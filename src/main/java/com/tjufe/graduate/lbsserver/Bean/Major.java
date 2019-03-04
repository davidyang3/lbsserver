package com.tjufe.graduate.lbsserver.Bean;

import lombok.Data;

import javax.persistence.*;

@Entity
@Table(name = "major")
@Data
public class Major {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int majorId;

    private String majorName;

    private int deptId;
}
