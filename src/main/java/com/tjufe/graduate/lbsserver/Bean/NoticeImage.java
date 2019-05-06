package com.tjufe.graduate.lbsserver.Bean;

import lombok.Data;

import javax.persistence.*;

@Entity
@Table(name = "NoticeImage")
@Data
public class NoticeImage {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "nimage_num")
    private int noticeImageId;

    @Column(name = "notice_id")
    private int noticeId;

    @Column(name = "nimage_name")
    private String imageName;

    @Column(name = "nimage_filename")
    private String imagePath;
}
