package com.tjufe.graduate.lbsserver.Bean;


import lombok.Data;

import javax.persistence.*;

@Entity
@Table(name = "department")
@Data
public class Department {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int departmentId;

    private String name;

    private String description;

    private String picturePath;

    private int buildingId;

    private String leaderId;

    private String superManagerId;

}
