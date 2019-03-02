package com.tjufe.graduate.lbsserver.Bean;

import lombok.Data;

import java.util.Date;
import java.util.List;

@Data
public class ActivityDetail {

    Integer activityId;

    String title;

    String content;

    User admin;

    User accessor;

    User publisher;

    String picturePath;

    Building building;

    double longitude;

    double latitude;

    Date startTime;

    Date endTime;

    Date createTime;

    int status;

    List<Tag> tagList;

    public ActivityDetail(Activity activity) {
        this.activityId = activity.getActivityId();
        this.content = activity.getContent();
        this.title = activity.getTitle();
        this.createTime = activity.getCreateTime();
        this.latitude = activity.getLatitude();
        this.longitude = activity.getLongitude();
        this.endTime = activity.getEndTime();
        this.picturePath = activity.getPicturePath();
        this.startTime = activity.getStartTime();
        this.status = activity.getStatus();
        this.tagList = activity.getTagList();
    }

}
