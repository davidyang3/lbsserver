package com.tjufe.graduate.lbsserver.Bean;


import lombok.Data;

import javax.persistence.*;

@Entity
@Table(name = "building")
@Data
public class Building {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "building_id")
    private int buildingId;

    @Column(name = "building_name")
    private String name;

    private double longitude;

    private double latitude;

    @Column(name = "building_image")
    private String picturePath;

    @Column(name = "building_describe")
    private String description;

    private int type;
}
