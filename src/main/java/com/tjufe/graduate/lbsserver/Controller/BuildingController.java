package com.tjufe.graduate.lbsserver.Controller;

import com.tjufe.graduate.lbsserver.Bean.Building;
import com.tjufe.graduate.lbsserver.Service.BuildingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
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

    @GetMapping("/type/{type:.+}/name/{name:.+}")
    public List<Building> getBuildingByTypeAndName(@PathVariable int type, @PathVariable String name) throws UnsupportedEncodingException {
        name = URLDecoder.decode(name, "utf-8");
        return buildingService.getBuildingByTypeAndName(type, name);
    }

    @GetMapping("/type/{type:.+}")
    public List<Building> getBuildingByType(@PathVariable int type) {
        return buildingService.getBuildingByTypeAndName(type, null);
    }

    @GetMapping("/name/{name:.+}")
    public List<Building> getBuildingByType(@PathVariable String name) throws UnsupportedEncodingException {
        name = URLDecoder.decode(name, "utf-8");
        return buildingService.getBuildingByName(name);
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
    @PostMapping("/update/picture/{id:.+}")
    public Building updatePicture(@PathVariable int id, @RequestBody String picture) {
        return buildingService.updatePicture(id, picture);
    }

    @ResponseBody
    @PostMapping("/update/description/{id:.+}/{description:.+}")
    public Building updateDescription(@PathVariable int id, @PathVariable String description) {
        return buildingService.updateDescription(id, description);
    }

}
