package com.tjufe.graduate.lbsserver.Bean;


import lombok.Data;

import javax.persistence.*;

@Entity
@Table(name = "department")
@Data
public class Department {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "higher_dept_id")
    private int departmentId;

    @Column(name = "dept_name")
    private String name;

    @Column(name = "dept_describe")
    private String description;

    private String picturePath;

    @Column(name = "building_id")
    private int buildingId;

    @Column(name = "leader_id")
    private String leaderId;

    @Column(name = "super_manager")
    private String superManagerId;

    @Column(name = "higher_dept_id")
    private int higherDeptId;
}
