package com.tjufe.graduate.lbsserver.Controller;

import com.tjufe.graduate.lbsserver.Bean.Department;
import com.tjufe.graduate.lbsserver.Bean.DepartmentDetail;
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
    public List<DepartmentDetail> list() {
        return departmentService.list();
    }

    @ResponseBody
    @GetMapping("/higher/{id:.+}/name/{name:.+}")
    public List<DepartmentDetail> getByHigherAndName(@PathVariable Integer id, @PathVariable String name) {
        return departmentService.getByHigherAndName(id, name);
    }

    @ResponseBody
    @GetMapping("/{id:.+}")
    public DepartmentDetail getById(@PathVariable int id) {
        return departmentService.queryById(id);
    }

    @ResponseBody
    @PostMapping("/update/picture/{id:.+}/{picture:.+}")
    public Department updatePicture(@PathVariable int id, @PathVariable String picture) {
        return departmentService.updatePicture(id, picture);
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
    @PostMapping("/update/buildingId/{id:.+}/{buildingId:.+}")
    public Department updateBuildingId(@PathVariable int id, @PathVariable int buildingId) {
        return departmentService.updateBuildingId(id, buildingId);
    }

    @ResponseBody
    @PostMapping("/update/higherDept/{id:.+}/{higher:.+}")
    public Department updateHigherDept(@PathVariable int id, @PathVariable int higher) {
        return departmentService.updateHigherDepartment(id, higher);
    }
}
