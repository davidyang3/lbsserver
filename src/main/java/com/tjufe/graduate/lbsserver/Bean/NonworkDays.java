package com.tjufe.graduate.lbsserver.Bean;

import lombok.Data;

import javax.persistence.*;
import java.sql.Date;

@Entity
@Table(name = "NonworkDays")
@Data
public class NonworkDays {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "off_days")
    private Date offDays;
}
