package com.tjufe.graduate.lbsserver.Bean;


import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "department")
@Data
public class Department {

    @Id
    int departmentId;

    String name;

    String description;

    String picturePath;

    int buildingId;

}
