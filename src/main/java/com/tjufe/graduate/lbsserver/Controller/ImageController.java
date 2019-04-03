package com.tjufe.graduate.lbsserver.Controller;

import com.tjufe.graduate.lbsserver.Service.ImageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/LBS/iamge")
public class ImageController {

    @Autowired
    ImageService imageService;

    @ResponseBody
    @GetMapping("/{path:.+}")
    public String getImage(@PathVariable String path) {
        return imageService.getImage(path);
    }
}
