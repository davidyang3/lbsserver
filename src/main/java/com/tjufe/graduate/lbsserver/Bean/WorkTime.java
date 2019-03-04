package com.tjufe.graduate.lbsserver.Bean;

import lombok.Data;

import javax.persistence.*;

@Entity
@Table(name = "WorkTime")
@Data
public class WorkTime {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Integer id;

    long workTimeStart;

    long workTimeEnd;
}
