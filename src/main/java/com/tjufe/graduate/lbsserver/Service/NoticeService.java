package com.tjufe.graduate.lbsserver.Service;

import com.tjufe.graduate.lbsserver.Bean.*;
import com.tjufe.graduate.lbsserver.Dao.*;
import jodd.util.StringUtil;
import lombok.extern.slf4j.Slf4j;
import org.assertj.core.util.Lists;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.*;
import java.util.stream.Collectors;

@Service
@Slf4j
public class NoticeService {

    @Autowired
    HobbyDao hobbyDao;

    @Autowired
    NoticeDao noticeDao;

    @Autowired
    TagDao tagDao;

    @Autowired
    AdminDao adminDao;

    @Autowired
    UserDao userDao;

    @Autowired
    TagNoticeMappingDao tagNoticeMappingDao;

    @Autowired
    NoticeImageDao noticeImageDao;

    @Autowired
    RedissonClient redissonClient;

    @Autowired
    ImageService imageService;

    public Notice create(Notice notice) {
        //todo: check validity
        if (notice.getNoticeId() != null) {
            Notice prev = noticeDao.findById(notice.getNoticeId()).get();
            prev.setUpdateTime(new Date());
            prev.setEndTime(notice.getEndTime());
            prev.setStartTime(notice.getStartTime());
            prev.setBuildingId(notice.getBuildingId());
            prev.setStatus(notice.getStatus());
            prev.setAssessor(notice.getAssessor());
            prev.setContent(notice.getContent());
            prev.setPicturePath(notice.getPicturePath());
            prev.setPriority(notice.getPriority());
            prev.setTitle(notice.getTitle());
            prev.setType(notice.getType());
            prev.setPublisher(notice.getPublisher());
            prev.setPublishTime(notice.getPublishTime());
            noticeDao.save(prev);
            tagNoticeMappingDao.deleteByNoticeId(notice.getNoticeId());
        } else {
            noticeDao.save(notice);
        }
        List<Integer> tagList = null;
        if (notice.getTagList() != null)
            tagList = notice.getTagList().stream().map(tag -> tag.getTagId()).collect(Collectors.toList());
        if (tagList != null) {
            tagList.forEach(tagId -> tagNoticeMappingDao.save(new TagNoticeMapping(Integer.valueOf(tagId),
                    notice.getNoticeId())));
        }
        return notice;
    }

    public NoticeDetail findById(int id) {
       Notice notice =  noticeDao.findById(id).get();
       return handleNoticeDetail(handleNotice(notice));
    }

    @Transactional
    public void delete(int noticeId) {
        noticeDao.deleteById(noticeId);
        tagNoticeMappingDao.deleteByNoticeId(noticeId);
        noticeImageDao.deleteByNoticeId(noticeId);
    }

    @Transactional
    public List<NoticeImage> updateNoticeImage(int noticeId, List<String> newList) {
        Notice notice = noticeDao.findById(noticeId).get();
        notice.setUpdateTime(new Date());
        noticeDao.save(notice);
        List<NoticeImage> oldList = noticeImageDao.findByNoticeId(noticeId);
        noticeImageDao.deleteByNoticeId(noticeId);
        newList.forEach(image -> {
            String picturePath = "notice/" + UUID.randomUUID();
            picturePath = imageService.saveImage(image, picturePath);
            NoticeImage noticeImage = new NoticeImage();
            noticeImage.setNoticeId(noticeId);
            noticeImage.setImagePath(picturePath);
            noticeImageDao.save(noticeImage);
        });
        return oldList;
    }



    public Notice handleNotice(Notice notice) {
        List<TagNoticeMapping> tagMappingList = tagNoticeMappingDao.findByNoticeId(notice.getNoticeId());
        List<Tag> tagList = Lists.newArrayList();
        tagMappingList.forEach(tagNoticeMapping -> {
            Optional<Tag> tagOptional = tagDao.findById(tagNoticeMapping.getTagId());
            if (tagOptional.isPresent()) {
                tagList.add(tagOptional.get());
            } else {
                log.error("tag: {} of notice: {} not exist", tagNoticeMapping.getTagId(),
                        tagNoticeMapping.getNoticeId());
            }
        });
        notice.setTagList(tagList);
        List<NoticeImage> noticeImages = noticeImageDao.findByNoticeId(notice.getNoticeId());
        notice.setImageList(noticeImages);
        return notice;
    }

    @Transactional
    public NoticeDetail handleNoticeDetail(Notice notice) {
        NoticeDetail noticeDetail = new NoticeDetail(notice);
        if (notice.getAssessor() != null) {
            noticeDetail.setAssessor(userDao.findById(notice.getAssessor()).get());
        }
        if (notice.getPublisher() != null) {
            noticeDetail.setPublisher(userDao.findById(notice.getPublisher()).get());
        }
        return noticeDetail;
    }

    public List<NoticeDetail> queryAll() {
        return noticeDao.findAll().stream().map(notice -> handleNotice(notice)).map(this::handleNoticeDetail)
                .collect(Collectors.toList());
    }

    public List<Notice> list() {
        return noticeDao.findAll().stream().map(notice -> handleNotice(notice)).collect(Collectors.toList());
    }

    public List<NoticeDetail> getByType(int type) {
        List<Notice> list = noticeDao.findByType(type);
        return list.stream().map(this::handleNotice).map(this::handleNoticeDetail).collect(Collectors.toList());
    }

    public List<NoticeDetail> getByStatus(int status) {
        List<Notice> list = noticeDao.findByStatus(status);
        return list.stream().map(this::handleNotice).map(this::handleNoticeDetail).collect(Collectors.toList());
    }

    public List<NoticeDetail> getByStatusAndTitle(int type, Integer status, String name) {
        List<Notice> list;
        if (status == null && StringUtil.isEmpty(name)) {
            list = noticeDao.findAll();
        } else if (status == null) {
            list = noticeDao.findByTypeAndTitleLike(type, name);
        } else if (StringUtil.isEmpty(name)) {
            list = noticeDao.findByTypeAndStatus(type, status);
        } else {
            list = noticeDao.findByTypeAndStatusAndTitleLike(type, status, name);
        }
        return list.stream().map(this::handleNotice).map(this::handleNoticeDetail).collect(Collectors.toList());
    }

    public List<NoticeDetail> getByPublisherAndStatus(String publisher, Integer status) {
        List<Notice> list = noticeDao.findByPublisherAndStatus(publisher, status);
        return list.stream().map(this::handleNotice).map(this::handleNoticeDetail).collect(Collectors.toList());
    }

    public List<NoticeDetail> getRecommandNotice(String userId, int start, int end) {
        List<Hobby> hobbies = hobbyDao.findByUserId(userId);
        List<Integer> tagIdList = hobbies.stream().map(hobby -> hobby.getHobbyId()).collect(Collectors.toList());
        List<TagNoticeMapping> mappings = tagNoticeMappingDao.findByTagIdIn(tagIdList);
        List<Integer> noticeIdList = mappings.stream().map(tagNoticeMapping -> tagNoticeMapping.getNoticeId())
                .collect(Collectors.toList());
        List<Notice> noticeList = list();
        List<Notice> canceledList = noticeList.stream().filter(notice ->
                // todo : status havent been decided;
                notice.getStatus() == 0
        ).collect(Collectors.toList());
        noticeList.removeAll(canceledList);
        List<Notice> firstList = noticeList.stream().filter(notice ->
                noticeIdList.contains(notice.getNoticeId())).collect(Collectors.toList());
        noticeList.removeAll(firstList);
        int firstCount = firstList.size();
        int secondCount = noticeList.size();
        List<Notice> result = new ArrayList<>(), part1 = new ArrayList<>(), part2 = new ArrayList<>(),
                part3 = new ArrayList<>();
        // recommend
        if (start < firstCount) {
            int to = firstCount < end ? firstCount : end;
            part1 = firstList.subList(start, to);
        }
        // in time
        if (firstCount < end && start < firstCount + secondCount) {
            int from = firstCount < start ? start - firstCount : 0;
            int to = end < firstCount + secondCount ? end - firstCount : secondCount;
            part2 = noticeList.subList(from, to);
        }
        // out time
        if (end > firstCount + secondCount) {
            int from = start > firstCount + secondCount ? start - firstCount - secondCount : 0;
            int to = end - firstCount- secondCount;
            to = to < canceledList.size() ? to : canceledList.size();
            part3 = canceledList.subList(from, to);
        }

        result.addAll(part1);
        result.addAll(part2);
        result.addAll(part3);
        return result.stream().map(notice -> handleNotice(notice)).map(this::handleNoticeDetail)
                .collect(Collectors.toList());

    }

    @Transactional
    public List<Integer> updateTagList(int noticeId, List<Integer> tagList) {
        Notice notice = noticeDao.findById(noticeId).get();
        notice.setUpdateTime(new Date());
        noticeDao.save(notice);
        List<TagNoticeMapping> mappingList = tagNoticeMappingDao.findByNoticeId(noticeId);

        if (mappingList == null) {
            mappingList = Lists.newArrayList();
        }
        List<Integer> oldTagList = mappingList.stream().map(tagNoticeMapping -> tagNoticeMapping.getTagId())
                .collect(Collectors.toList());

        tagNoticeMappingDao.deleteByNoticeId(noticeId);
        tagList.forEach(tag -> {
            TagNoticeMapping tagNoticeMapping = new TagNoticeMapping(tag, noticeId);
            tagNoticeMappingDao.save(tagNoticeMapping);
        });
        return oldTagList;
    }

    /**
     * todo: should check status from and to can or cannot be realization
     * @param noticeId
     * @param status
     * @return new notice
     */
    @Transactional
    public Notice updateStatus(int noticeId, int status) {
        Optional<Notice> noticeOptional = noticeDao.findById(Integer.valueOf(noticeId));
        if (noticeOptional.isPresent()) {
            Notice notice = handleNotice(noticeOptional.get());
            notice.setStatus(status);
            notice.setUpdateTime(new Date());
            // todo: check validity
            noticeDao.save(notice);
            return notice;
        } else {
            log.error("notice: {} not exist", noticeId);
            return null;
        }
    }

    @Transactional
    public Notice updatePicture(int noticeId, String picture) {
        Optional<Notice> noticeOptional = noticeDao.findById(Integer.valueOf(noticeId));
        if (noticeOptional.isPresent()) {
            Notice notice = handleNotice(noticeOptional.get());
            String picturePath = "notice/" + UUID.randomUUID();
            picturePath = imageService.saveImage(picture, picturePath);
            notice.setUpdateTime(new Date());
            notice.setPicturePath(picturePath);
            // todo: check validity
            noticeDao.save(notice);
            return notice;
        } else {
            log.error("notice: {} not exist", noticeId);
            return null;
        }
    }

    @Transactional
    public Notice examine(String userId, int status, int noticeId) {
        Optional<Notice> noticeOptional = noticeDao.findById(Integer.valueOf(noticeId));
        if (noticeOptional.isPresent()) {
            Notice notice = handleNotice(noticeOptional.get());
            notice.setStatus(status);
            notice.setAssessor(userId);
            // todo: check validity
            noticeDao.save(notice);
            return notice;
        } else {
            log.error("notice: {} not exist", noticeId);
            return null;
        }
    }

    @Transactional
    public Notice updateTitle(int noticeId, String title) {
        Optional<Notice> noticeOptional = noticeDao.findById(Integer.valueOf(noticeId));
        if (noticeOptional.isPresent()) {
            Notice notice = handleNotice(noticeOptional.get());
            // todo: set status updating
            notice.setStatus(0);
            notice.setUpdateTime(new Date());
            notice.setTitle(title);
            // todo: check validity
            noticeDao.save(notice);
            return notice;
        } else {
            log.error("notice: {} not exist", noticeId);
            return null;
        }
    }

    @Transactional
    public Notice updateContent(int noticeId, String title) {
        Optional<Notice> noticeOptional = noticeDao.findById(Integer.valueOf(noticeId));
        if (noticeOptional.isPresent()) {
            Notice notice = handleNotice(noticeOptional.get());
            // todo: set status updating
            notice.setStatus(0);
            notice.setContent(title);
            // todo: check validity
            noticeDao.save(notice);
            return notice;
        } else {
            log.error("notice: {} not exist", noticeId);
            return null;
        }
    }

    @Transactional
    public Notice updateType(int noticeId, int type) {
        Optional<Notice> noticeOptional = noticeDao.findById(Integer.valueOf(noticeId));
        if (noticeOptional.isPresent()) {
            Notice notice = handleNotice(noticeOptional.get());
            notice.setType(type);
            notice.setUpdateTime(new Date());
            // todo: check validity
            noticeDao.save(notice);
            return notice;
        } else {
            log.error("notice: {} not exist", noticeId);
            return null;
        }
    }

    @Transactional
    public Notice updatePriority(int noticeId, int priority) {
        Optional<Notice> noticeOptional = noticeDao.findById(Integer.valueOf(noticeId));
        if (noticeOptional.isPresent()) {
            Notice notice = handleNotice(noticeOptional.get());
            notice.setPriority(priority);
            notice.setUpdateTime(new Date());
            // todo: check validity
            noticeDao.save(notice);
            return notice;
        } else {
            log.error("notice: {} not exist", noticeId);
            return null;
        }
    }

    @Transactional
    public Notice updateBuildingId(int noticeId, int buildingId) {
        Optional<Notice> noticeOptional = noticeDao.findById(Integer.valueOf(noticeId));
        if (noticeOptional.isPresent()) {
            Notice notice = handleNotice(noticeOptional.get());
            notice.setBuildingId(buildingId);
            notice.setUpdateTime(new Date());
            // todo: check validity
            noticeDao.save(notice);
            return notice;
        } else {
            log.error("notice: {} not exist", noticeId);
            return null;
        }
    }



    @Transactional
    public Notice updateStartEndTime(int noticeId, long startTime, long endTime) {
        Optional<Notice> noticeOptional = noticeDao.findById(Integer.valueOf(noticeId));
        if (noticeOptional.isPresent()) {
            Notice notice = handleNotice(noticeOptional.get());
            notice.setStartTime(new Date(startTime));
            notice.setEndTime(new Date(endTime));
            notice.setUpdateTime(new Date());
            // todo: check validity
            noticeDao.save(notice);
            return notice;
        } else {
            log.error("notice: {} not exist", noticeId);
            return null;
        }
    }
}
