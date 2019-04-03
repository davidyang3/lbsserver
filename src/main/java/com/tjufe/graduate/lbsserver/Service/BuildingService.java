package com.tjufe.graduate.lbsserver.Service;

import com.tjufe.graduate.lbsserver.Bean.Building;
import com.tjufe.graduate.lbsserver.Dao.BuildingDao;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@Slf4j
public class BuildingService {

    @Autowired
    BuildingDao buildingDao;

    @Autowired
    RedissonClient redissonClient;

    @Autowired
    ImageService imageService;

    public Building create(Building building) {
        // todo: check validity
        buildingDao.save(building);
        return building;
    }

    /**
     * todo: foreign key or delete all dependencies
     * @param buildingId
     */
    public void delete(int buildingId) {
        buildingDao.deleteById(Integer.valueOf(buildingId));
    }

    public List<Building> list() {
        return buildingDao.findAll();
    }

    public Building queryById(int buildingId) {
        Optional<Building> buildingOptional = buildingDao.findById(Integer.valueOf(buildingId));
        return buildingOptional.isPresent() ? buildingOptional.get() : null;
    }

    @Transactional
    public Building updatePicture(int buildingId, String picture) {
        Building building = buildingDao.getOne(buildingId);
        if (building != null) {
            String picturePath = "building/" + UUID.randomUUID();
            picturePath = imageService.saveImage(picture, picturePath);
            building.setPicturePath(picturePath);
            // todo: check validity
            buildingDao.save(building);
            return building;
        } else {
            log.error("building:{} not exist", buildingId);
            return null;
        }
    }

    @Transactional
    public Building updateDescription(int buildingId, String description) {
        Building building = buildingDao.getOne(buildingId);
        if (building != null) {
            building.setDescription(description);
            // todo: check validity
            buildingDao.save(building);
            return building;
        } else {
            log.error("building:{} not exist", buildingId);
            return null;
        }
    }

}
