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
    Integer id;

    Date offDays;
}
