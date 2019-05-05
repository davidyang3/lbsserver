package com.tjufe.graduate.lbsserver.Bean;

import lombok.Data;

import javax.persistence.*;

@Entity
@Table(name = "WorkTime")
@Data
public class WorkTime {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "worktime_start")
    private long workTimeStart;

    @Column(name = "worktime_end")
    private long workTimeEnd;
}
