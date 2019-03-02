package com.tjufe.graduate.lbsserver.Bean;


import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "notice")
@Data
public class Notice {

    @Id
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
    List<Tag> tagList;
}
