package com.tjufe.graduate.lbsserver.Service;

import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ImageService {

    @Autowired
    RedissonClient redissonClient;

    public String getImage(String path) {
        return redissonClient.getBucket(path).get().toString();
    }
}
