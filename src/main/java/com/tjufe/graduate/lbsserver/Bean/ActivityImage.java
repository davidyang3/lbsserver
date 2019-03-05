package com.tjufe.graduate.lbsserver.Bean;


import lombok.Data;

import javax.persistence.*;

@Entity
@Table(name = "activityImage")
@Data
public class ActivityImage {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private int activityId;

    private String imageNum;

    private String imageName;

    private String imagePath;

}
