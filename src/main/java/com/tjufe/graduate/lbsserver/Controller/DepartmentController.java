package com.tjufe.graduate.lbsserver.Controller;

import com.tjufe.graduate.lbsserver.Bean.Department;
import com.tjufe.graduate.lbsserver.Service.DepartmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/LBS/department")
public class DepartmentController {

    @Autowired
    DepartmentService departmentService;

    @ResponseBody
    @PutMapping
    public Department create(@RequestBody Department department) {
        return departmentService.create(department);
    }

    @DeleteMapping(value = "/{departmentId:.+}")
    public void delete(@PathVariable int departmentId) {
        departmentService.delete(departmentId);
    }

    @ResponseBody
    @GetMapping("/list")
    public List<Department> list() {
        return departmentService.list();
    }

    @ResponseBody
    @GetMapping("/{id:.+}")
    public Department getById(@PathVariable int id) {
        return departmentService.queryById(id);
    }

    @ResponseBody
    @PostMapping("/update/picturePath/{id:.+}/{picturePath:.+}")
    public Department updatePicturePath(@PathVariable int id, @PathVariable String picturePath) {
        return departmentService.updatePicturePath(id, picturePath);
    }

    @ResponseBody
    @PostMapping("/update/description/{id:.+}/{description:.+}")
    public Department updateDescription(@PathVariable int id, @PathVariable String description) {
        return departmentService.updateDescription(id, description);
    }

    @ResponseBody
    @PostMapping("/update/name/{id:.+}/{name:.+}")
    public Department updateName(@PathVariable int id, @PathVariable String name) {
        return departmentService.updateName(id, name);
    }

    @ResponseBody
    @PostMapping("/update/name/{id:.+}/{buildingId:.+}")
    public Department updateBuildingId(@PathVariable int id, @PathVariable int buildingId) {
        return departmentService.updateBuildingId(id, buildingId);
    }
}
