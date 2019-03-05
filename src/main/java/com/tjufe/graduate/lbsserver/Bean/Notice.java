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
    private int noticeId;

    private String title;

    private String content;

    private int adminId;

    private String publisher;

    private String assessor;

    private Date publishTime;

    private Date createTime;

    private String picturePath;

    private int type;

    private int priority;

    private int status;

    @Transient
    private List<NoticeImage> imageList;

    @Transient
    private List<Tag> tagList;
}
