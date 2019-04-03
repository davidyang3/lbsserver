package com.tjufe.graduate.lbsserver.Service;

import com.tjufe.graduate.lbsserver.Bean.*;
import com.tjufe.graduate.lbsserver.Dao.*;
import lombok.extern.slf4j.Slf4j;
import org.assertj.core.util.Lists;
import org.assertj.core.util.Sets;
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
        noticeDao.save(notice);
        List<Integer> tagList = notice.getTagList().stream().map(tag ->
                tag.getTagId()).collect(Collectors.toList());
        if (tagList != null) {
            tagList.forEach(tagId -> tagNoticeMappingDao.save(new TagNoticeMapping(Integer.valueOf(tagId),
                    notice.getNoticeId())));
        }
        return notice;
    }

    @Transactional
    public void delete(int noticeId) {
        noticeDao.deleteById(noticeId);
        tagNoticeMappingDao.deleteByNoticeId(noticeId);
        noticeImageDao.deleteByNoticeId(noticeId);
    }

    @Transactional
    public List<NoticeImage> updateNoticeImage(int noticeId, List<String> newList) {
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
        noticeDetail.setAdmin(userDao.findById(adminDao.findById(notice.getAdminId()).get().getUserId()).get());
        noticeDetail.setAssessor(userDao.findById(notice.getAssessor()).get());
        noticeDetail.setPublisher(userDao.findById(notice.getPublisher()).get());
        return noticeDetail;
    }

    public List<NoticeDetail> queryAll() {
        return noticeDao.findAll().stream().map(notice -> handleNotice(notice)).map(this::handleNoticeDetail)
                .collect(Collectors.toList());
    }

    public List<Notice> list() {
        return noticeDao.findAll().stream().map(notice -> handleNotice(notice)).collect(Collectors.toList());
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
        List<TagNoticeMapping> mappingList = tagNoticeMappingDao.findByNoticeId(noticeId);
        if (mappingList == null) {
            mappingList = Lists.newArrayList();
        }
        List<Integer> oldTagList = mappingList.stream().map(tagNoticeMapping -> tagNoticeMapping.getTagId())
                .collect(Collectors.toList());
        Set<Integer> set = Sets.newHashSet();
        set.addAll(tagList);
        set.addAll(oldTagList);
        List<Integer> toDelete = Lists.newArrayList();
        List<Integer> toAdd = Lists.newArrayList();
        set.forEach(tagId -> {
            if (oldTagList.contains(tagId) && !tagList.contains(tagId)) {
                toDelete.add(tagId);
            } else if (!oldTagList.contains(tagId) && tagList.contains(tagId)) {
                toAdd.add(tagId);
            }
        });
        toDelete.forEach(tagId -> tagNoticeMappingDao.deleteByTagIdAndNoticeId(tagId, noticeId));
        toAdd.forEach(tagId -> tagNoticeMappingDao.save(new TagNoticeMapping(noticeId, tagId)));
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
    public Notice updateAdminId(int noticeId, int adminId) {
        Optional<Notice> noticeOptional = noticeDao.findById(Integer.valueOf(noticeId));
        if (noticeOptional.isPresent()) {
            Notice notice = handleNotice(noticeOptional.get());
            notice.setAdminId(adminId);
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
            // todo: check validity
            noticeDao.save(notice);
            return notice;
        } else {
            log.error("notice: {} not exist", noticeId);
            return null;
        }
    }

}
