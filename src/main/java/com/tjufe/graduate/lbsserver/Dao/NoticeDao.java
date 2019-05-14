package com.tjufe.graduate.lbsserver.Dao;

import com.tjufe.graduate.lbsserver.Bean.Notice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface NoticeDao extends JpaRepository<Notice, Integer> {

    List<Notice> findByTypeAndStatusAndTitleContaining(int type, int status, String name);

    List<Notice> findByTypeAndStatus(int type, int status);

    List<Notice> findByTypeAndTitleContaining(int type, String name);

    List<Notice> findByPublisherAndStatus(String publisher, int status);

    List<Notice> findByType(int type);

    List<Notice> findByStatus(int status);
}
