package com.tjufe.graduate.lbsserver.Dao;


import com.tjufe.graduate.lbsserver.Bean._Class;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ClassDao extends JpaRepository<_Class, Integer> {

    List<_Class> findByMajorId(int majorId);
}
