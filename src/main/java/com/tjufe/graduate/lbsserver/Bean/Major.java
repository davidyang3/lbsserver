package com.tjufe.graduate.lbsserver.Bean;

import lombok.Data;

import javax.persistence.*;

@Entity
@Table(name = "major")
@Data
public class Major {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "major_id")
    private int majorId;

    @Column(name = "major_name")
    private String majorName;

    @Column(name = "pastern_id")
    private int deptId;
}
