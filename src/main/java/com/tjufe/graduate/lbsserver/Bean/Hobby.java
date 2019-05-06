package com.tjufe.graduate.lbsserver.Bean;


import lombok.Data;

import javax.persistence.*;

@Entity
@Table(name = "user_hobby")
@Data
public class Hobby {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "user_id")
    private String userId;

    @Column(name = "hobby_id")
    private int hobbyId;

    public Hobby(String userId, int hobbyId) {
        this.hobbyId = hobbyId;
        this.userId = userId;
    }

}
