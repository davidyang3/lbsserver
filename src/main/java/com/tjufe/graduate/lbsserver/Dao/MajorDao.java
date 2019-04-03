package com.tjufe.graduate.lbsserver.Dao;

import com.tjufe.graduate.lbsserver.Bean.Major;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MajorDao extends JpaRepository<Major, Integer> {

    List<Major> findByDeptId(int deptId);
}