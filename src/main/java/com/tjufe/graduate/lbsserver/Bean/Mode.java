package com.tjufe.graduate.lbsserver.Bean;

import lombok.Data;

import javax.persistence.*;

@Entity
@Table(name = "mode")
@Data
public class Mode {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int modeId;

    String modeState;
}
