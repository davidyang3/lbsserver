package com.tjufe.graduate.lbsserver.Bean;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;

import java.util.*;
import java.util.stream.Collectors;
import java.util.zip.CRC32;
import java.util.zip.Checksum;

@Slf4j
public class UserCache {

    private Set<UserStatus> userStatusSet;

    public String checksum;

    private static ObjectMapper objectMapper = new ObjectMapper();

    public UserCache(List<UserStatus> set) {
        userStatusSet = new TreeSet<>(Comparator.comparing(UserStatus::getUserId));
        this.userStatusSet.addAll(set);
        this.checksum = calculateChecksum(this.userStatusSet);
    }

    private static String calculateChecksum(Set<UserStatus> users) {
        try {
            Checksum checksum = new CRC32();
            byte[] bytes = objectMapper.writeValueAsBytes(users);
            checksum.update(bytes, 0, bytes.length);
            return Long.toString(checksum.getValue());
        } catch (JsonProcessingException e) {
            return UUID.randomUUID().toString();
        }
    }

    public void updateUser(UserStatus userStatus) {
        userStatusSet = userStatusSet.stream().filter(status -> !status.getUserId().equals(userStatus.getUserId()))
                .collect(Collectors.toSet());
        userStatusSet.add(userStatus);
        checksum = calculateChecksum(userStatusSet);
    }

    public void deleteUser(String userId) {
        Optional<UserStatus> user = userStatusSet.stream().filter(u -> u.getUserId().equals(userId)).findAny();
        user.ifPresent(u -> {
            userStatusSet.remove(u);
            checksum = calculateChecksum(userStatusSet);
        });
    }

    public UserStatus getUserStatus(String userId) {
        Optional<UserStatus> user = userStatusSet.stream().filter(u -> u.getUserId().equals(userId)).findAny();
        return user.get();
    }
}
