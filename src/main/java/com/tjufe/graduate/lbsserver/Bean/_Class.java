package com.tjufe.graduate.lbsserver.Bean;

import lombok.Data;

import javax.persistence.*;

@Entity
@Table(name = "class")
@Data
public class _Class {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "class_id")
    private int classId;

    @Column(name = "class_name")
    private String className;

    @Column(name = "major_id")
    private int majorId;
}
