package com.tjufe.graduate.lbsserver.Bean;


import lombok.Data;

import javax.persistence.*;

@Entity
@Table(name = "building")
@Data
public class Building {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int buildingId;

    private String name;

    private double longitude;

    private double latitude;

    private String picturePath;

    private String description;

    private int type;
}
