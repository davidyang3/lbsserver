package com.tjufe.graduate.lbsserver.Bean;

import lombok.Data;

import java.util.List;

@Data
public class UserDetail {String userId;

    String userName;

    String nickName;

    String password;

    int status;

    int type;

    String sex;

    String idNumber;

    String telNumber;

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
        this.portraitPath = user.getPortraitPath();
        this.isStudent = user.isStudent();
        this.userImage = user.getUserImage();
        this.hobbyList = getHobbyList();
    }

    String email;

    String portraitPath;

    boolean isStudent;

    String className;

    String majorName;

    String DeptName;

    String departmentName;

    int departmentId;

    String userImage;

    String position;

    List<Integer> hobbyList;
}
