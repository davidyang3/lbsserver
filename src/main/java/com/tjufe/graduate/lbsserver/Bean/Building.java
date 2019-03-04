package com.tjufe.graduate.lbsserver.Bean;


import lombok.Data;

import javax.persistence.*;

@Entity
@Table(name = "building")
@Data
public class Building {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    int buildingId;

    String name;

    double longitude;

    double latitude;

    String picturePath;

    String description;

    int type;
}
