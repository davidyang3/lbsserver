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
    @Column(name = "notice_id")
    private Integer noticeId;

    private String title;

    private String content;

    private String publisher;

    private String assessor;

    @Column(name = "publish_time")
    private Date publishTime;

    @Column(name = "start_time")
    private Date startTime;

    @Column(name = "end_time")
    private Date endTime;

    @Column(name = "update_time")
    private Date updateTime;

    @Column(name = "notice_image")
    private String picturePath;

    @Column(name = "building_id")
    private Integer buildingId;

    private int type;

    private int priority;

    @Column(name = "notice_state")
    private int status;

    @Transient
    private List<NoticeImage> imageList;

    @Transient
    private List<Tag> tagList;
}
