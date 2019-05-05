package com.tjufe.graduate.lbsserver.Dao;

import com.tjufe.graduate.lbsserver.Bean.Department;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface DepartmentDao extends JpaRepository<Department, Integer> {

    List<Department> findByHigherDeptIdAndName(int higherDeptId, String name);

    List<Department> findByHigherDeptId(int higherDeptId);

    List<Department> findByName(String name);
}
