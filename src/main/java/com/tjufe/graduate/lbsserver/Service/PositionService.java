package com.tjufe.graduate.lbsserver.Service;

import com.tjufe.graduate.lbsserver.Bean.Position;
import com.tjufe.graduate.lbsserver.Bean.User;
import com.tjufe.graduate.lbsserver.Bean.UserStatus;
import javafx.geometry.Pos;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RBucket;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.concurrent.TimeUnit;

@Service
@Slf4j
public class PositionService {

    @Autowired
    RedissonClient redissonClient;

    @Autowired
    UserService userService;

    private static final String LBS_REDIS_KEY_PREFIX = "com.tjufe.lbs";

    @Transactional
    public Position updatePosition(Position position){
        RBucket rBucket = redissonClient.getBucket(LBS_REDIS_KEY_PREFIX + position.getUserId());
        rBucket.set(position, 10, TimeUnit.SECONDS);
        log.info("updatePosition   userId:{},longitude:{},latitude:{}", position.getUserId(), position.getLongitude(), position.getLatitude());
        return position;
    }

    private boolean enablePosition(long startLong, long endLong) {
        Date start = new Date(startLong);
        Date end = new Date(endLong);
        Date now = new Date();
        int nowHour = now.getHours();
        int nowMinute = now.getMinutes();
        int nowSecond = now.getSeconds();
        int startHour = start.getHours();
        int startMinute = start.getMinutes();
        int startSecond = start.getSeconds();
        int endHour = end.getHours();
        int endMinute = end.getMinutes();
        int endSecond = end.getSeconds();
        long n = nowHour * 60 * 60 + nowMinute * 60 + nowSecond;
        long s = startHour * 60 * 60 + startMinute * 60 + startSecond;
        long e = endHour * 60 * 60 + endMinute * 60 + endSecond;
        if (n >= s && n <= e) {
            return true;
        } else {
            return false;
        }
    }

    public Position getPosition(String userId){
        log.info("getPosition   userId:{}", userId);
        UserStatus userStatus = userService.getUserStatus(userId);
        if (userStatus.getStatus() == 0 || (userStatus.getStatus() == 1 &&
                enablePosition(userStatus.getStartTime(), userStatus.getEndTime()))) {
            RBucket rBucket = redissonClient.getBucket(LBS_REDIS_KEY_PREFIX + userId);
            Position position = (Position) rBucket.get();
            return position;
        } else {
            Position position = new Position();
            position.setUserId(userId);
            return position;
        }
    }

}
