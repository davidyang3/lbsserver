package com.tjufe.graduate.lbsserver.Bean;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "position")
@Data
public class Position {

    @Id
    private String userId;

    private double longitude;

    private double latitude;
}
