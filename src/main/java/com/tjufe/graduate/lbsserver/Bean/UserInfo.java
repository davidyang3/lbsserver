package com.tjufe.graduate.lbsserver.Bean;

import lombok.Data;

import javax.persistence.Transient;
import java.util.List;

@Data
public class UserInfo {

    private String userId;

    private String userName;

    private String nickName;

    private String password;

    private int status;

    private int type;

    private String sex;

    private String idNumber;

    private String telNumber;

    private int isValid;

    private String email;

    private String userImage;

    private int departmentId;

    private String position;

    private String title;

    private String role;

    private int classId;

    @Transient
    private List<Integer> hobbyList;

    public static User getUser(UserInfo userInfo) {
        User user = new User();
        user.setUserId(userInfo.getUserId());
        user.setIsValid(userInfo.getIsValid());
        user.setPassword(userInfo.getPassword());
        user.setUserName(userInfo.getUserName());
        user.setEmail(userInfo.getEmail());
        user.setHobbyList(userInfo.getHobbyList());
        user.setNickName(userInfo.getNickName());
        user.setStatus(userInfo.getStatus());
        user.setType(userInfo.getType());
        user.setTelNumber(userInfo.getTelNumber());
        user.setIdNumber(userInfo.getIdNumber());
        user.setSex(userInfo.getSex());
        user.setUserImage(userInfo.getUserImage());
        return user;
    }
}
