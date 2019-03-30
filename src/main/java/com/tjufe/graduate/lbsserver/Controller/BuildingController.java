package com.tjufe.graduate.lbsserver.Controller;

import com.tjufe.graduate.lbsserver.Bean.Building;
import com.tjufe.graduate.lbsserver.Service.BuildingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/LBS/building")
public class BuildingController {

    @Autowired
    BuildingService buildingService;

    @ResponseBody
    @PutMapping
    public Building create(@RequestBody Building building) {
        return buildingService.create(building);
    }

    @DeleteMapping(value = "/{buildingId:.+}")
    public void delete(@PathVariable int buildingId) {
        buildingService.delete(buildingId);
    }

    @ResponseBody
    @GetMapping("/list")
    public List<Building> list() {
        return buildingService.list();
    }

    @ResponseBody
    @GetMapping("/{id:.+}")
    public Building getById(@PathVariable int id) {
        return buildingService.queryById(id);
    }

    @ResponseBody
    @PostMapping("/update/picture/{id:.+}/{picture:.+}")
    public Building updatePicture(@PathVariable int id, @PathVariable String picture) {
        return buildingService.updatePicture(id, picture);
    }

    @ResponseBody
    @PostMapping("/update/description/{id:.+}/{description:.+}")
    public Building updateDescription(@PathVariable int id, @PathVariable String description) {
        return buildingService.updateDescription(id, description);
    }

}
