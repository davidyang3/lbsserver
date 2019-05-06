package com.tjufe.graduate.lbsserver.Bean;

import lombok.Data;

import java.util.Date;
import java.util.List;

@Data
public class NoticeDetail {

    private int noticeId;

    private String title;

    private String content;

    private User admin;

    private User publisher;

    private User assessor;

    private Date publishTime;

    private Date createTime;

    private String picturePath;

    private int type;

    private int priority;

    private int status;

    private List<Tag> tagList;

    private List<NoticeImage> imageList;

    private int buildingId;

    private Date startTime;

    private Date endTime;

    private Date updateTime;

    public NoticeDetail(Notice notice) {
        this.noticeId = notice.getNoticeId();
        this.title = notice.getTitle();
        this.content = notice.getContent();
        this.publishTime = notice.getPublishTime();
        this.picturePath = notice.getPicturePath();
        this.type = notice.getType();
        this.priority = notice.getPriority();
        this.status = notice.getStatus();
        this.tagList = notice.getTagList();
        this.imageList = notice.getImageList();
        this.updateTime = notice.getUpdateTime();
        this.startTime = notice.getStartTime();
        this.endTime = notice.getEndTime();
        this.buildingId = notice.getNoticeId();
    }
}
