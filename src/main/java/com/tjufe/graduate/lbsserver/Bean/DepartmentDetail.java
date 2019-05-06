package com.tjufe.graduate.lbsserver.Bean;

import lombok.Data;

@Data
public class DepartmentDetail {

    private int departmentId;

    private String name;

    private String description;

    private String picturePath;

    private Building building;

    private UserDetail leader;

    private UserDetail superManager;

    private int higherDeptId;

    public DepartmentDetail(Department department) {
        this.departmentId = department.getDepartmentId();
        this.description = department.getDescription();
        this.picturePath = department.getPicturePath();
        this.name = department.getName();
        this.higherDeptId = department.getHigherDeptId();
    }
}
