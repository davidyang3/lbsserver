package com.tjufe.graduate.lbsserver.Bean;


import lombok.Data;

import javax.persistence.*;

@Entity
@Table(name = "hobby")
@Data
public class Hobby {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String userId;

    private int hobbyId;

    public Hobby(String userId, int hobbyId) {
        this.hobbyId = hobbyId;
        this.userId = userId;
    }

}
