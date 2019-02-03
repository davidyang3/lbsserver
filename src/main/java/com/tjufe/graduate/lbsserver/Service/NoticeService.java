package com.tjufe.graduate.lbsserver.Service;

import com.tjufe.graduate.lbsserver.Bean.Hobby;
import com.tjufe.graduate.lbsserver.Bean.Notice;
import com.tjufe.graduate.lbsserver.Bean.Tag;
import com.tjufe.graduate.lbsserver.Bean.TagNoticeMapping;
import com.tjufe.graduate.lbsserver.Dao.HobbyDao;
import com.tjufe.graduate.lbsserver.Dao.NoticeDao;
import com.tjufe.graduate.lbsserver.Dao.TagDao;
import com.tjufe.graduate.lbsserver.Dao.TagNoticeMappingDao;
import lombok.extern.slf4j.Slf4j;
import org.assertj.core.util.Lists;
import org.assertj.core.util.Sets;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;
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
    TagNoticeMappingDao tagNoticeMappingDao;

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

    public void delete(int noticeId) {
        noticeDao.deleteById(noticeId);
        tagNoticeMappingDao.deleteByNoticeId(noticeId);
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
        return notice;
    }

    public List<Notice> queryAll() {
        return noticeDao.findAll().stream().map(notice -> handleNotice(notice)).collect(Collectors.toList());
    }

    public List<Notice> getRecommandNotice(String userId, int start, int end) {
        List<Hobby> hobbies = hobbyDao.findByUserId(userId);
        List<Integer> tagIdList = hobbies.stream().map(hobby -> hobby.getHobbyId()).collect(Collectors.toList());
        List<TagNoticeMapping> mappings = tagNoticeMappingDao.findByTagIdIn(tagIdList);
        List<Integer> noticeIdList = mappings.stream().map(tagNoticeMapping -> tagNoticeMapping.getNoticeId())
                .collect(Collectors.toList());
        List<Notice> noticeList = queryAll();
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
        result = result.stream().map(notice -> handleNotice(notice)).collect(Collectors.toList());
        return result;
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
    public Notice updatePicturePath(int noticeId, String picturePath) {
        Optional<Notice> noticeOptional = noticeDao.findById(Integer.valueOf(noticeId));
        if (noticeOptional.isPresent()) {
            Notice notice = handleNotice(noticeOptional.get());
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
    public Notice updateTitle(int noticeId, String title) {
        Optional<Notice> noticeOptional = noticeDao.findById(Integer.valueOf(noticeId));
        if (noticeOptional.isPresent()) {
            Notice notice = handleNotice(noticeOptional.get());
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
