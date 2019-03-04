package com.tjufe.graduate.lbsserver.Dao;

import com.tjufe.graduate.lbsserver.Bean.Dept;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DeptDao extends JpaRepository<Dept, Integer> {
}