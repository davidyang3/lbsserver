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
    private int Id;

    private String userId;

    private long startTime;

    private long endTime;
}
