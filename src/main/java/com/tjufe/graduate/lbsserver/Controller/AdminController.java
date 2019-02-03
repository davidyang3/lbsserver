package com.tjufe.graduate.lbsserver.Controller;

import com.tjufe.graduate.lbsserver.Bean.Admin;
import com.tjufe.graduate.lbsserver.Service.AdminService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * todo: should add auth to modify api
 */
@RestController
@RequestMapping("/LBS/admin")
public class AdminController {

    @Autowired
    AdminService adminService;

    @ResponseBody
    @PutMapping
    public Admin create(Admin admin) {
        return adminService.create(admin);
    }

    @DeleteMapping(value = "/{adminId:.+}")
    public void delete(@PathVariable int adminId) {
        adminService.delete(adminId);
    }

    @ResponseBody
    @GetMapping("/list")
    public List<Admin> list() {
        return adminService.queryAll();
    }

    @ResponseBody
    @GetMapping("/department/{id:.+}")
    public List<Admin> query(@PathVariable int id) {
        return adminService.queryByDepartmentId(id);
    }

    @ResponseBody
    @PostMapping("/update/{adminId:.+}/role/{role:.+}")
    public Admin updateRole(@PathVariable int adminId, @PathVariable int role) {
        return adminService.updateRole(adminId, role);
    }
}
