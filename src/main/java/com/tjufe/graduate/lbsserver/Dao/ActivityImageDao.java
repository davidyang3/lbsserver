package com.tjufe.graduate.lbsserver.Dao;

import com.tjufe.graduate.lbsserver.Bean.ActivityImage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ActivityImageDao extends JpaRepository<ActivityImage, Integer> {
    List<ActivityImage> findByActivityId(int activityId);

    @Modifying
    @Query(value = "delete from activity_image where activity_id = ?1", nativeQuery = true)
    int deleteByActivityId(int activityId);
}
