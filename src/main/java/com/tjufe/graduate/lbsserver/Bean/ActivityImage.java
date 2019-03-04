package com.tjufe.graduate.lbsserver.Bean;


import lombok.Data;

import javax.persistence.*;

@Entity
@Table(name = "activityImage")
@Data
public class ActivityImage {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Integer id;

    int activityId;

    String imageNum;

    String imageName;

    String imagePath;

}
