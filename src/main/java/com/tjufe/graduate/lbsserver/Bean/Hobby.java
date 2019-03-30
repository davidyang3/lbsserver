package com.tjufe.graduate.lbsserver.Bean;


import lombok.Data;

import javax.persistence.*;

@Entity
@Table(name = "user_hobby")
@Data
public class Hobby {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Integer id;

    String userId;

    int hobbyId;

    public Hobby(String userId, int hobbyId) {
        this.hobbyId = hobbyId;
        this.userId = userId;
    }

}
