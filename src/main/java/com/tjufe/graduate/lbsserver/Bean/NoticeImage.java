package com.tjufe.graduate.lbsserver.Bean;

import lombok.Data;

import javax.persistence.*;

@Entity
@Table(name = "NoticeImage")
@Data
public class NoticeImage {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int noticeId;

    private String imageNum;

    private String imageName;

    private String imagePath;
}
