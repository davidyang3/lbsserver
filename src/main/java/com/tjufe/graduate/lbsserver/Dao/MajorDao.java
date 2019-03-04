package com.tjufe.graduate.lbsserver.Dao;

import com.tjufe.graduate.lbsserver.Bean.Major;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MajorDao extends JpaRepository<Major, Integer> {
}