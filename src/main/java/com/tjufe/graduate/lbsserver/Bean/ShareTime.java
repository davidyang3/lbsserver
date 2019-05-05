package com.tjufe.graduate.lbsserver.Bean;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Table(name = "shateTime")
@Data
@NoArgsConstructor
public class ShareTime {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "user_id")
    private String userId;

    @Column(name = "start_time")
    private long startTime;

    @Column(name = "end_time")
    private long endTime;
}
