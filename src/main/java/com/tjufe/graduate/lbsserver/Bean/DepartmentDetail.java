package com.tjufe.graduate.lbsserver.Bean;

import lombok.Data;

@Data
public class DepartmentDetail {

    int departmentId;

    String name;

    String description;

    String picturePath;

    Building building;

    UserDetail leader;

    UserDetail superManager;

    public DepartmentDetail(Department department) {
        this.departmentId = department.getDepartmentId();
        this.description = department.getDescription();
        this.picturePath = department.getPicturePath();
    }
}
