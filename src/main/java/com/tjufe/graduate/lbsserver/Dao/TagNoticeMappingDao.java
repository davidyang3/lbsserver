package com.tjufe.graduate.lbsserver.Dao;

import com.tjufe.graduate.lbsserver.Bean.TagNoticeMapping;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TagNoticeMappingDao extends JpaRepository<TagNoticeMapping, Integer> {


    List<TagNoticeMapping> findByTagIdIn(List<Integer> list);

    List<TagNoticeMapping> findByNoticeId(Integer noticeId);

    @Modifying
    @Query(value = "delete from tag_notice_mapping where tag_id =?1 and notice_id = ?2", nativeQuery = true)
    int deleteByTagIdAndNoticeId(int tagId, int noticeId);

    @Modifying
    @Query(value = "delete from tag_notice_mapping where notice_id = ?1", nativeQuery = true)
    int deleteByNoticeId(int noticeId);
}
