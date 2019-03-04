package com.tjufe.graduate.lbsserver.Bean;

import lombok.Data;

import java.util.Date;
import java.util.List;

@Data
public class NoticeDetail {

    int noticeId;

    String title;

    String content;

    User admin;

    User publisher;

    User assessor;

    Date publishTime;

    Date createTime;

    String picturePath;

    int type;

    int priority;

    int status;

    List<Tag> tagList;

    List<NoticeImage> imageList;

    public NoticeDetail(Notice notice) {
        this.noticeId = notice.getNoticeId();
        this.title = notice.getTitle();
        this.content = notice.getContent();
        this.publishTime = notice.getPublishTime();
        this.createTime = notice.getCreateTime();
        this.picturePath = notice.getPicturePath();
        this.type = notice.getType();
        this.priority = notice.getPriority();
        this.status = notice.getStatus();
        this.tagList = notice.getTagList();
        this.imageList = notice.getImageList();
    }
}
