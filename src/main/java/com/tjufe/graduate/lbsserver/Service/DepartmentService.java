package com.tjufe.graduate.lbsserver.Service;

import com.tjufe.graduate.lbsserver.Bean.Department;
import com.tjufe.graduate.lbsserver.Bean.DepartmentDetail;
import com.tjufe.graduate.lbsserver.Bean.UserDetail;
import com.tjufe.graduate.lbsserver.Dao.BuildingDao;
import com.tjufe.graduate.lbsserver.Dao.DepartmentDao;
import jodd.util.StringUtil;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@Slf4j
public class DepartmentService {

    @Autowired
    DepartmentDao departmentDao;

    @Autowired
    BuildingDao buildingDao;

    @Autowired
    UserService userService;

    @Autowired
    RedissonClient redissonClient;

    @Autowired
    ImageService imageService;

    public Department create(Department department) {
        // todo: check validity
        departmentDao.save(department);
        return department;
    }

    /**
     * todo: foreign key or delete all dependencies
     * @param departmentId
     */
    public void delete(int departmentId) {
        departmentDao.deleteById(Integer.valueOf(departmentId));
    }

    public List<DepartmentDetail> list() {
        return departmentDao.findAll().stream().map(this::handleDepartment).collect(Collectors.toList());
    }

    public List<DepartmentDetail> getByHigherAndName(Integer id, String name) {
        List<Department> list;
        if (id == null && StringUtil.isEmpty(name)) {
            list = departmentDao.findAll();
        } else if (id == null) {
            list = departmentDao.findByNameLike(name);
        } else if (StringUtil.isEmpty(name)) {
            list = departmentDao.findByHigherDeptId(id);
        } else {
            list = departmentDao.findByHigherDeptIdAndNameLike(id, name);
        }
        return list.stream().map(department -> this.handleDepartment(department)).collect(Collectors.toList());
    }

    private DepartmentDetail handleDepartment(Department department) {
        DepartmentDetail departmentDetail = new DepartmentDetail(department);
        if (department.getBuildingId() != null) {
            departmentDetail.setBuilding(buildingDao.findById(department.getBuildingId()).get());
        }
        if (department.getLeaderId() != null) {
            departmentDetail.setLeader(userService.queryWithId(department.getLeaderId()));
        }
        if (department.getSuperManagerId() != null) {
            departmentDetail.setSuperManager(userService.queryWithId(department.getLeaderId()));
        }
        return departmentDetail;
    }

    public DepartmentDetail queryById(int id) {
        Optional<Department> departmentOptional = departmentDao.findById(id);
        return departmentOptional.isPresent() ? handleDepartment(departmentOptional.get()) : null;
    }


    @Transactional
    public Department updatePicture(int id, String picture) {
        Department department = departmentDao.getOne(id);
        if (department != null) {
            String picturePath = "department/" + UUID.randomUUID();
            picturePath = imageService.saveImage(picture, picturePath);
            department.setPicturePath(picturePath);
            // todo: check validity
            departmentDao.save(department);
            return department;
        } else {
            log.error("department:{} not exist", id);
            return null;
        }
    }

    @Transactional
    public Department updateDescription(int id, String description) {
        Department department = departmentDao.getOne(id);
        if (department != null) {
            department.setDescription(description);
            // todo: check validity
            departmentDao.save(department);
            return department;
        } else {
            log.error("department:{} not exist", id);
            return null;
        }
    }

    @Transactional
    public Department updateName(int id, String name) {
        Department department = departmentDao.getOne(id);
        if (department != null) {
            department.setName(name);
            // todo: check validity
            departmentDao.save(department);
            return department;
        } else {
            log.error("department:{} not exist", id);
            return null;
        }
    }

    public Department updateHigherDepartment(int id, int higher) {
        Department department = departmentDao.getOne(id);
        if (department != null) {
            department.setHigherDeptId(higher);
            // todo: check validity
            departmentDao.save(department);
            return department;
        } else {
            log.error("department:{} not exist", id);
            return null;
        }
    }

    @Transactional
    public Department updateBuildingId(int id, int buildingId) {
        Department department = departmentDao.getOne(id);
        if (department != null) {
            department.setBuildingId(buildingId);
            // todo: check validity
            departmentDao.save(department);
            return department;
        } else {
            log.error("department:{} not exist", id);
            return null;
        }
    }

}
