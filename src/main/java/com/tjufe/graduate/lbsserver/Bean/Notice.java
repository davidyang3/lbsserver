package com.tjufe.graduate.lbsserver.Bean;


import lombok.Data;

import javax.persistence.*;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "notice")
@Data
public class Notice {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    int noticeId;

    String title;

    String content;

    int adminId;

    String publisher;

    String assessor;

    Date publishTime;

    Date createTime;

    String picturePath;

    int type;

    int priority;

    int status;

    @Transient
    List<NoticeImage> imageList;

    @Transient
    List<Tag> tagList;
}
