package com.tjufe.graduate.lbsserver.Bean;

import lombok.Data;

import javax.persistence.*;

@Entity
@Table(name = "student")
@Data
public class Student {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int studentId;

    @Column(name = "user_id")
    private String userId;

    @Column(name = "class_id")
    private int classId;
}
