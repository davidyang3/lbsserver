package com.tjufe.graduate.lbsserver.Bean;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class UserStatus {
    String userId;

    int status;

    long startTime;

    long endTime;
}
