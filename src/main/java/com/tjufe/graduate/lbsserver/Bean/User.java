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
    private String userId;

    private String userName;

    private String nickName;

    private String password;

    private int status;

    private int type;

    private String sex;

    private String idNumber;

    private String telNumber;

    private String email;

    private String UserImage;

    private String portraitPath;

    private boolean isStudent;

    @Transient
    private List<Integer> hobbyList;
}
