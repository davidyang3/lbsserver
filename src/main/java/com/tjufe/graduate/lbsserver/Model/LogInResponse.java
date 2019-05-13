package com.tjufe.graduate.lbsserver.Model;

import com.tjufe.graduate.lbsserver.Bean.ShareTime;
import com.tjufe.graduate.lbsserver.Bean.User;
import lombok.AllArgsConstructor;
import lombok.Data;
import java.util.List;

@Data
@AllArgsConstructor
public class LogInResponse {

    User user;

    ShareTime shareTime;

    List<Integer> hobbyList;

    int status;

}
