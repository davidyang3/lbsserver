package com.tjufe.graduate.lbsserver.Dao;

import com.tjufe.graduate.lbsserver.Bean.Staff;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface StaffDao extends JpaRepository<Staff, String> {

    Staff findByUserId(String userId);

    List<Staff> findByDepartmentId(int departmentId);
}