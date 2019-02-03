package com.tjufe.graduate.lbsserver.Dao;

import com.tjufe.graduate.lbsserver.Bean.TagActivityMapping;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TagActivityMappingDao extends JpaRepository<TagActivityMapping, Integer> {

    List<TagActivityMapping> findByTagIdIn(List<Integer> list);

    List<TagActivityMapping> findByActivityId(Integer activityId);

    @Modifying
    @Query(value = "delete from tag_activity_mapping where tag_id =?1 and activity_id = ?2", nativeQuery = true)
    int deleteByTagIdAndActivityId(int tagId, int ActivityId);

    @Modifying
    @Query(value = "delete from tag_activity_mapping where activity_id = ?1", nativeQuery = true)
    int deleteByActivityId(int activityId);
}
