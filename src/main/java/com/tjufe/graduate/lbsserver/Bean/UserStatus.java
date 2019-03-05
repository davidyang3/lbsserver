package com.tjufe.graduate.lbsserver.Bean;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class UserStatus {
    private String userId;

    private int status;

    private long startTime;

    private long endTime;
}
