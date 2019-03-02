package com.tjufe.graduate.lbsserver.Bean;


import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "building")
@Data
public class Building {

    @Id
    int buildingId;

    String name;

    double longitude;

    double latitude;

    String picturePath;

    String description;

    int type;
}
