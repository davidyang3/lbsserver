package com.tjufe.graduate.lbsserver.Service;

import com.tjufe.graduate.lbsserver.Bean.*;
import com.tjufe.graduate.lbsserver.Dao.*;
import lombok.extern.slf4j.Slf4j;
import org.assertj.core.util.Lists;
import org.assertj.core.util.Sets;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

@Service
@Slf4j
public class ActivityService {

    @Autowired
    ActivityDao activityDao;

    @Autowired
    TagActivityMappingDao tagActivityMappingDao;

    @Autowired
    HobbyDao hobbyDao;

    @Autowired
    TagDao tagDao;

    @Autowired
    BuildingDao buildingDao;

    private boolean isInRange(Activity activity, double longitude, double latitude, double radius) {
        double radiusSquare = radius * radius;
        double activityLatitude = 0, activityLongitude = 0;
        if (activity.getBuildingId() != null) {
            Optional<Building> buildingOptional = buildingDao.findById(activity.getActivityId());
            if (buildingOptional.isPresent()) {
                activityLatitude = buildingOptional.get().getLatitude();
                activityLongitude = buildingOptional.get().getLongtitude();
            } else {
                log.error("building: {} of activity: {} not exist", activity.getBuildingId(), activity.getActivityId());
                return false;
            }
        } else {
            activityLatitude = activity.getLatitude();
            activityLongitude = activity.getLongitude();
        }
        double distanceSquare = (activityLatitude - latitude) * (activityLatitude - latitude) +
                (activityLongitude - longitude) * (activityLongitude - longitude);
        return distanceSquare <= radiusSquare;
    }

    private Activity handleActivity(Activity activity) {
        List<TagActivityMapping> tagMappingList = tagActivityMappingDao.findByActivityId(activity.getActivityId());
        List<Tag> tagList = Lists.newArrayList();
        tagMappingList.forEach(tagActivityMapping -> {
            Optional<Tag> tagOptional = tagDao.findById(tagActivityMapping.getTagId());
            if (tagOptional.isPresent()) {
                tagList.add(tagOptional.get());
            } else {
                log.error("tag: {} of activity: {} not exist", tagActivityMapping.getTagId(),
                        tagActivityMapping.getActivityId());
            }
        });
        activity.setTagList(tagList);
        return activity;
    }

    public List<Activity> getActivityList() {
        List<Activity> activityList = Lists.newArrayList(activityDao.findAll());
        return activityList.stream().map(this::handleActivity).collect(Collectors.toList());
    }

    public List<Activity> getInTimeList() {
        List<Activity> activityList = activityDao.findActivitiesInTime(new Date(System.currentTimeMillis()));
        return activityList.stream().map(this::handleActivity).collect(Collectors.toList());
    }

    public List<Activity> getCurrentList() {
        List<Activity> activityList = activityDao.findCurrentActivities(new Date(System.currentTimeMillis()));
        return activityList.stream().map(this::handleActivity).collect(Collectors.toList());
    }

    public List<Activity> getActivityInRange(double longitude, double latitude, double radius) {
        List<Activity> list = getCurrentList();
        List<Activity> result = list.stream().filter(activity -> isInRange(activity, longitude, latitude, radius))
                .collect(Collectors.toList());
        return result;
    }

    public List<Activity> getActivityListByUserId(String userId, int start, int end) {
        List<Hobby> hobbies = hobbyDao.findByUserId(userId);
        List<Integer> tagIdList = hobbies.stream().map(hobby -> hobby.getHobbyId()).collect(Collectors.toList());
        List<TagActivityMapping> mappings = tagActivityMappingDao.findByTagIdIn(tagIdList);
        List<Integer> activityIdList = mappings.stream().map(tagActivityMapping -> tagActivityMapping.getActivityId())
                .collect(Collectors.toList());
        List<Activity> activityList = getActivityList();
        List<Activity> outTimeList = activityList.stream().filter(activity ->
            // todo : status havent been decided;
            activity.getEndTime().getTime() < System.currentTimeMillis() || activity.getStatus() == 0
        ).collect(Collectors.toList());
        activityList.removeAll(outTimeList);
        List<Activity> firstList = activityList.stream().filter(activity ->
            activityIdList.contains(activity.getActivityId())).collect(Collectors.toList());
        activityList.removeAll(firstList);
        int firstCount = firstList.size();
        int secondCount = activityList.size();
        List<Activity> result = new ArrayList<>(), part1 = new ArrayList<>(), part2 = new ArrayList<>(),
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
            part2 = activityList.subList(from, to);
        }
        // out time
        if (end > firstCount + secondCount) {
            int from = start > firstCount + secondCount ? start - firstCount - secondCount : 0;
            int to = end - firstCount- secondCount;
            to = to < outTimeList.size() ? to : outTimeList.size();
            part3 = outTimeList.subList(from, to);
        }

        result.addAll(part1);
        result.addAll(part2);
        result.addAll(part3);
        result = result.stream().map(activity -> handleActivity(activity)).collect(Collectors.toList());
        return result;
    }

    public Activity createActivity(Activity activity) {
        activity.setActivityId(null);
        // todo: check validity
        activityDao.save(activity);
        List<Integer> tagList = activity.getTagList().stream().map(tag ->
                tag.getTagId()).collect(Collectors.toList());
        if (tagList != null) {
            tagList.forEach(tagId -> tagActivityMappingDao.save(new TagActivityMapping(tagId,
                    activity.getActivityId())));
        }
        return activity;
    }

    @Transactional
    public Activity updateTitle(int activityId, String title) {
        Optional<Activity> activityOptional = activityDao.findById(Integer.valueOf(activityId));
        if (activityOptional.isPresent()) {
            Activity activity = handleActivity(activityOptional.get());
            activity.setTitle(title);
            // todo: check validity
            activityDao.save(activity);
            return activity;
        } else {
            log.error("activity: {} not exist", activityId);
            // throw new LBSExceptons.NoSuchActivity(activityId);
            return null;
        }
    }

    @Transactional
    public Activity updateContent(int activityId, String title) {
        Optional<Activity> activityOptional = activityDao.findById(Integer.valueOf(activityId));
        if (activityOptional.isPresent()) {
            Activity activity = handleActivity(activityOptional.get());
            activity.setContent(title);
            // todo: check validity
            activityDao.save(activity);
            return activity;
        } else {
            log.error("activity: {} not exist", activityId);
            // throw new LBSExceptons.NoSuchActivity(activityId);
            return null;
        }
    }

    @Transactional
    public Activity updateAdminId(int activityId, int adminId) {
        Optional<Activity> activityOptional = activityDao.findById(Integer.valueOf(activityId));
        if (activityOptional.isPresent()) {
            Activity activity = handleActivity(activityOptional.get());
            activity.setAdminId(adminId);
            // todo: check validity
            activityDao.save(activity);
            return activity;
        } else {
            log.error("activity: {} not exist", activityId);
            // throw new LBSExceptons.NoSuchActivity(activityId);
            return null;
        }
    }

    @Transactional
    public Activity updatePicturePath(int activityId, String picturePath) {
        Optional<Activity> activityOptional = activityDao.findById(Integer.valueOf(activityId));
        if (activityOptional.isPresent()) {
            Activity activity = handleActivity(activityOptional.get());
            activity.setPicturePath(picturePath);
            // todo: check validity
            activityDao.save(activity);
            return activity;
        } else {
            log.error("activity: {} not exist", activityId);
            // throw new LBSExceptons.NoSuchActivity(activityId);
            return null;
        }
    }

    @Transactional
    public Activity updateStartTime(int activityId, Date startTime) {
        Optional<Activity> activityOptional = activityDao.findById(Integer.valueOf(activityId));
        if (activityOptional.isPresent()) {
            Activity activity = handleActivity(activityOptional.get());
            activity.setStartTime(startTime);
            // todo: check validity
            activityDao.save(activity);
            return activity;
        } else {
            log.error("activity: {} not exist", activityId);
            // throw new LBSExceptons.NoSuchActivity(activityId);
            return null;
        }
    }

    @Transactional
    public Activity updateEndTime(int activityId, Date endTime) {
        Optional<Activity> activityOptional = activityDao.findById(Integer.valueOf(activityId));
        if (activityOptional.isPresent()) {
            Activity activity = handleActivity(activityOptional.get());
            activity.setEndTime(endTime);
            // todo: check validity
            activityDao.save(activity);
            return activity;
        } else {
            log.error("activity: {} not exist", activityId);
            // throw new LBSExceptons.NoSuchActivity(activityId);
            return null;
        }
    }

    /**
     * todo: should check status from and to can or cannot be realization
     * @param activityId
     * @param status
     * @return new acitivity
     */
    @Transactional
    public Activity updateStatus(int activityId, int status) {
        Optional<Activity> activityOptional = activityDao.findById(Integer.valueOf(activityId));
        if (activityOptional.isPresent()) {
            Activity activity = handleActivity(activityOptional.get());
            activity.setStatus(status);
            // todo: check validity
            activityDao.save(activity);
            return activity;
        } else {
            log.error("activity: {} not exist", activityId);
            // throw new LBSExceptons.NoSuchActivity(activityId);
            return null;
        }
    }

    @Transactional
    public List<Integer> updateTagList(int activityId, List<Integer> tagList) {
        List<TagActivityMapping> mappingList = tagActivityMappingDao.findByActivityId(activityId);
        if (mappingList == null) {
            mappingList = Lists.newArrayList();
        }
        List<Integer> oldTagList = mappingList.stream().map(tagActivityMapping -> tagActivityMapping.getTagId())
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
        toDelete.forEach(tagId -> tagActivityMappingDao.deleteByTagIdAndActivityId(tagId, activityId));
        toAdd.forEach(tagId -> tagActivityMappingDao.save(new TagActivityMapping(activityId, tagId)));
        return oldTagList;
    }

    /**
     * todo: really delete or set status ?
     * @param activityId
     */
    public void deleteActivity(int activityId) {
       activityDao.deleteById(activityId);
       tagActivityMappingDao.deleteByActivityId(activityId);
    }

}
