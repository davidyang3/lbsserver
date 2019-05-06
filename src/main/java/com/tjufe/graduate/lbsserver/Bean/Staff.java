package com.tjufe.graduate.lbsserver.Bean;

import lombok.Data;

import javax.persistence.*;

@Entity
@Table(name = "staff")
@Data
public class Staff {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "staff_id")
    private int staffId;

    @Column(name = "user_id")
    private int userId;

    @Column(name = "dept_id")
    private int departmentId;

    private String position;

    private String title;

    private String role;

    @Column(name = "is_valid")
    private String isValid;
}
