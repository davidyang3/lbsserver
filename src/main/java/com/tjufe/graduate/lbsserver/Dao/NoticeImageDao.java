package com.tjufe.graduate.lbsserver.Dao;

import com.tjufe.graduate.lbsserver.Bean.NoticeImage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface NoticeImageDao extends JpaRepository<NoticeImage, Integer> {
    List<NoticeImage> findByNoticeId(int noticeId);

    @Modifying
    @Query(value = "delete from notice_image where notice_id = ?1", nativeQuery = true)
    int deleteByNoticeId(int noticeId);
}
