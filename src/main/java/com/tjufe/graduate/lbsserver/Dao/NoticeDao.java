package com.tjufe.graduate.lbsserver.Dao;

import com.tjufe.graduate.lbsserver.Bean.Notice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface NoticeDao extends JpaRepository<Notice, Integer> {
}
