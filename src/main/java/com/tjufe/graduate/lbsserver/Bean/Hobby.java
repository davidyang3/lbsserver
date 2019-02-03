package com.tjufe.graduate.lbsserver.Bean;


import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "hobby")
@Data
public class Hobby {

    @Id
    Integer id;

    String userId;

    int hobbyId;

    public Hobby(String userId, int hobbyId) {
        this.hobbyId = hobbyId;
        this.userId = userId;
    }

}
