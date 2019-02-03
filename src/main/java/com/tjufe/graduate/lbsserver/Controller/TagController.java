package com.tjufe.graduate.lbsserver.Controller;

import com.tjufe.graduate.lbsserver.Bean.Tag;
import com.tjufe.graduate.lbsserver.Service.TagService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/LBS/tag")
public class TagController {

    @Autowired
    TagService tagService;

    @ResponseBody
    @GetMapping
    public List<Tag> list() {
        return tagService.list();
    }

    @ResponseBody
    @PutMapping
    public Tag create(@RequestBody Tag tag) {
        return tagService.create(tag);
    }

    @DeleteMapping(value = "/{tagId:.+}")
    public void delete(@PathVariable int tagId) {
        tagService.delete(tagId);
    }

}
