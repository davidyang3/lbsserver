package com.tjufe.graduate.lbsserver.Dao;

import com.tjufe.graduate.lbsserver.Bean.ShareTime;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ShareTimeDao extends JpaRepository<ShareTime, Integer> {

    List<ShareTime> findByUserId(String userId);
}
