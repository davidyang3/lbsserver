package com.tjufe.graduate.lbsserver.Bean;

import lombok.Data;

import javax.persistence.Column;
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
    @Column(name = "user_id")
    private String userId;

    @Column(name = "user_name")
    private String userName;

    @Column(name = "nick_name")
    private String nickName;

    private String password;

    private int status;

    @Column(name = "user_type")
    private int type;

    private String sex;

    @Column(name = "id_number")
    private String idNumber;

    @Column(name = "phone")
    private String telNumber;

    private String email;

    private String userImage;

    @Column(name = "user_avatar")
    private String portraitPath;

    @Transient
    private List<Integer> hobbyList;
}
