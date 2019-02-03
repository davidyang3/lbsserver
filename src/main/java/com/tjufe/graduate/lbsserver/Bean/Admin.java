package com.tjufe.graduate.lbsserver.Bean;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "admin")
@Data
public class Admin {

    @Id
    Integer adminId;

    String userId;

    int role;

    int departmentId;

}
