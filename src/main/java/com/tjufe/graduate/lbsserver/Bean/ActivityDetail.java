package com.tjufe.graduate.lbsserver.Bean;

import lombok.Data;

import java.util.Date;
import java.util.List;

@Data
public class ActivityDetail {

    private Integer activityId;

    private String title;

    private String content;

    private UserDetail admin;

    private UserDetail accessor;

    private UserDetail publisher;

    private String picturePath;

    private Building building;

    private double longitude;

    private double latitude;

    private Date startTime;

    private Date endTime;

    private Date createTime;

    private int status;

    private List<ActivityImage> imageList;

    private List<Tag> tagList;

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
        this.imageList = activity.getImageList();
    }

}
