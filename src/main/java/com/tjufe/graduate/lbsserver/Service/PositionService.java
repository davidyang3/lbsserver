package com.tjufe.graduate.lbsserver.Service;

import com.tjufe.graduate.lbsserver.Bean.Position;
import com.tjufe.graduate.lbsserver.Dao.PositionDao;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Slf4j
public class PositionService {

    @Autowired
    private PositionDao positionDao;

    @Transactional
    public Position updatePosition(Position position){
        positionDao.save(position);
        log.info("updatePosition   userId:{},longtitude:{},latitude:{}", position.getUserId(), position.getLongitude(), position.getLatitude());
        return position;
    }

    public Position getPosition(String userId){
        log.info("getPosition   userId:{}", userId);
        return positionDao.findById(userId).orElse(null);
    }

}
