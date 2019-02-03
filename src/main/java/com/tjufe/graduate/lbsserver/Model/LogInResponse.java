package com.tjufe.graduate.lbsserver.Model;

import com.tjufe.graduate.lbsserver.Bean.User;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class LogInResponse {

    User user;

    int status;

}
