package com.tjufe.graduate.lbsserver.Controller;

import com.tjufe.graduate.lbsserver.Bean.Position;
import com.tjufe.graduate.lbsserver.Service.PositionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "/LBS/position")
public class PositionController {

    @Autowired
    private PositionService positionServie;

    @ResponseBody
    @RequestMapping(method = RequestMethod.PUT)
    public Position updatePositon(@RequestBody Position position){
        return positionServie.updatePosition(position);
    }

    @ResponseBody
    @RequestMapping(method = RequestMethod.GET, value = "/{userId}")
    public Position getPosition(@PathVariable String userId){
        return positionServie.getPosition(userId);
    }
}
