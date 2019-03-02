package com.tjufe.graduate.lbsserver.Service;

import com.tjufe.graduate.lbsserver.Bean.Position;
import javafx.geometry.Pos;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RBucket;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.concurrent.TimeUnit;

@Service
@Slf4j
public class PositionService {

    @Autowired
    RedissonClient redissonClient;

    private static final String LBS_REDIS_KEY_PREFIX = "com.tjufe.lbs";

    @Transactional
    public Position updatePosition(Position position){
        RBucket rBucket = redissonClient.getBucket(LBS_REDIS_KEY_PREFIX + position.getUserId());
        rBucket.set(position, 10, TimeUnit.SECONDS);
        log.info("updatePosition   userId:{},longitude:{},latitude:{}", position.getUserId(), position.getLongitude(), position.getLatitude());
        return position;
    }

    public Position getPosition(String userId){
        log.info("getPosition   userId:{}", userId);
        RBucket rBucket = redissonClient.getBucket(LBS_REDIS_KEY_PREFIX + userId);
        Position position = (Position)rBucket.get();
        return position;
    }

}
