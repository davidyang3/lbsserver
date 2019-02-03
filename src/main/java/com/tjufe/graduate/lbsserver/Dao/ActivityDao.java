package com.tjufe.graduate.lbsserver.Dao;

import com.tjufe.graduate.lbsserver.Bean.Activity;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Repository
public interface ActivityDao extends CrudRepository<Activity, Integer> {

    @Query(value = "select * from activity where end_time > ?1", nativeQuery = true)
    List<Activity> findActivitiesInTime(Date now);

    @Query(value = "select * from activity where end_time > ?1 and start_time < ?1", nativeQuery = true)
    List<Activity> findCurrentActivities(Date now);
}
