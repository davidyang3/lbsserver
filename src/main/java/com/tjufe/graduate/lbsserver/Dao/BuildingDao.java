package com.tjufe.graduate.lbsserver.Dao;

import com.tjufe.graduate.lbsserver.Bean.Building;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BuildingDao extends JpaRepository<Building, Integer> {

    List<Building> findBuildingByTypeAndNameLike(int type, String name);

    List<Building> findBuildingByType(int type);

    List<Building> findBuildingByNameLike(String name);
}
