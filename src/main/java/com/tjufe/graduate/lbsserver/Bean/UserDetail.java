package com.tjufe.graduate.lbsserver.Bean;

import lombok.Data;

import java.util.List;

@Data
public class UserDetail {String userId;

    private String userName;

    private String nickName;

    private String password;

    private int status;

    private int type;

    private String sex;

    private String idNumber;

    private String telNumber;

    public UserDetail(User user) {
        this.userId = user.getUserId();
        this.userName = user.getUserName();
        this.nickName = user.getNickName();
        this.password = user.getPassword();
        this.status = user.getStatus();
        this.type = user.getType();
        this.sex = user.getSex();
        this.idNumber = user.getIdNumber();
        this.telNumber = user.getTelNumber();
        this.email = user.getEmail();
        this.userImage = user.getUserImage();
        this.hobbyList = getHobbyList();
        this.is_valid = user.getIsValid();
    }

    String email;

    int is_valid;

    String className;

    String majorName;

    String DeptName;

    String departmentName;

    int departmentId;

    String userImage;

    String position;

    List<Integer> hobbyList;
}
