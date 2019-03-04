package com.tjufe.graduate.lbsserver.Bean;


import lombok.Data;

import javax.persistence.*;

@Entity
@Table(name = "department")
@Data
public class Department {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    int departmentId;

    String name;

    String description;

    String picturePath;

    int buildingId;

    String leaderId;

    String superManagerId;

}
