package com.tjufe.graduate.lbsserver.Service;

import com.tjufe.graduate.lbsserver.Bean.Admin;
import com.tjufe.graduate.lbsserver.Dao.AdminDao;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Slf4j
public class AdminService {

    @Autowired
    AdminDao adminDao;

    public Admin create(Admin admin) {
        // todo: check validity
        adminDao.save(admin);
        return admin;
    }

    public void delete(int adminId) {
        adminDao.deleteById(adminId);
    }

    public List<Admin> queryAll() {
        return adminDao.findAll();
    }

    public List<Admin> queryByDepartmentId(int departmentId) {
        return adminDao.findByDepartmentId(departmentId);
    }

    @Transactional
    public Admin updateRole(int adminId, int role) {
        Optional<Admin> adminOptional = adminDao.findById(adminId);
        if (adminOptional.isPresent()) {
            Admin admin = adminOptional.get();
            admin.setRole(role);
            // todo: check validity
            adminDao.save(admin);
            return admin;
        } else {
            log.error("admin:{} not exist", adminId);
            return null;
        }
    }
}
