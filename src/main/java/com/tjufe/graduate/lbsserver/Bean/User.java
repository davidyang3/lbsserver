package com.tjufe.graduate.lbsserver.Bean;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;
import java.util.List;

@Entity
@Table(name = "user")
@Data
public class User {

    @Id
    String userId;

    String userName;

    String nickName;

    String password;

    int status;

    int type;

    String sex;

    String idNumber;

    String telNumber;

    String email;

    String UserImage;

    String portraitPath;

    boolean isStudent;

    @Transient
    List<Integer> hobbyList;
}
