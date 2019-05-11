package com.tjufe.graduate.lbsserver.Controller;

import com.tjufe.graduate.lbsserver.Bean.Notice;
import com.tjufe.graduate.lbsserver.Bean.NoticeDetail;
import com.tjufe.graduate.lbsserver.Bean.NoticeImage;
import com.tjufe.graduate.lbsserver.Model.Pager;
import com.tjufe.graduate.lbsserver.Service.NoticeService;
import org.aspectj.weaver.ast.Not;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/LBS/notice")
public class NoticeController {

    @Autowired
    NoticeService noticeService;

    @ResponseBody
    @GetMapping(value = "/list")
    public List<NoticeDetail> getList() {
        return noticeService.queryAll();
    }

    @ResponseBody
    @GetMapping(value = "/id/{id:.+}")
    public NoticeDetail getById(@PathVariable int id) {
        return noticeService.findById(id);
    }

    @ResponseBody
    @PostMapping(value = "/list/pager/{userId:.+}")
    public List<NoticeDetail> getWithPager(@PathVariable String userId, @RequestBody Pager pager) {
        return noticeService.getRecommandNotice(userId, pager.getStart(), pager.getEnd());
    }

    @ResponseBody
    @GetMapping(value = "/type/{type:.+}")
    public List<NoticeDetail> getByType(@PathVariable int type) {
        return noticeService.getByType(type);
    }

    @ResponseBody
    @GetMapping(value = "/type/{type:.+}/status/{status:.+}")
    public List<NoticeDetail> getByStatus(@PathVariable int type, @PathVariable Integer status) {
        return noticeService.getByStatusAndTitle(type, status, null);
    }

    @ResponseBody
    @GetMapping(value = "/type/{type:.+}/title/{title:.+}")
    public List<NoticeDetail> getByTitle(@PathVariable int type, @PathVariable String title) {
        return noticeService.getByStatusAndTitle(type, null, title);
    }

    @ResponseBody
    @GetMapping(value = "/type/{type:.+}/status/{status:.+}/title/{title:.+}")
    public List<NoticeDetail> getByStatusAndTitle(@PathVariable int type, @PathVariable Integer status, @PathVariable String title) {
        return noticeService.getByStatusAndTitle(type, status, title);
    }

    @ResponseBody
    @RequestMapping(method = RequestMethod.PUT)
    public Notice create(@RequestBody Notice notice) {
        return noticeService.create(notice);
    }

    @ResponseBody
    @PostMapping(value = "/update/title/{id:.+}/{title:.+}")
    public Notice updateTitle(@PathVariable int id, @PathVariable String title) {
        return noticeService.updateTitle(id, title);
    }

    @ResponseBody
    @PostMapping(value = "/update/buildingId/{id:.+}/{buildingId:.+}")
    public Notice updateTitle(@PathVariable int id, @PathVariable int buildingId) {
        return noticeService.updateBuildingId(id, buildingId);
    }


    @ResponseBody
    @PostMapping(value = "/update/{id:.+}/startEndTime/{st:.+}/{et:.+}")
    public Notice updateTitle(@PathVariable int id, @PathVariable long st, @PathVariable long et) {
        return noticeService.updateBuildingId(id, st, et);
    }

    @ResponseBody
    @PostMapping(value = "/update/content/{id:.+}/{content:.+}")
    public Notice updateContent(@PathVariable int id, @PathVariable String content) {
        return noticeService.updateContent(id, content);
    }

    @ResponseBody
    @PostMapping(value = "/update/picture/{id:.+}/{picture:.+}")
    public Notice updatePicture(@PathVariable int id, @PathVariable String picture) {
        return noticeService.updatePicture(id, picture);
    }

    @ResponseBody
    @PostMapping(value = "/update/status/{id:.+}/{status:.+}")
    public Notice updateStatus(@PathVariable int id, @PathVariable int status) {
        return noticeService.updateStatus(id, status);
    }

    @ResponseBody
    @PostMapping(value = "/update/tag/{id:.+}")
    public List<Integer> updateTagList(@PathVariable int id, @RequestBody List<Integer> tagList) {
        return noticeService.updateTagList(id, tagList);
    }

    @ResponseBody
    @PostMapping(value = "/update/type/{id:.+}/{type:.+}")
    public Notice updateType(@PathVariable int id, @PathVariable int type) {
        return noticeService.updateType(id, type);
    }

    @ResponseBody
    @PostMapping(value = "/update/priority/{id:.+}/{priority:.+}")
    public Notice updatePriority(@PathVariable int id, @PathVariable int priority) {
        return noticeService.updatePriority(id, priority);
    }

    @ResponseBody
    @PostMapping(value = "/update/image/{id:.+}")
    public List<NoticeImage> updateImageList(@PathVariable int id, @RequestBody List<String> imageList) {
        return noticeService.updateNoticeImage(id, imageList);
    }

    @ResponseBody
    @PostMapping(value = "/examine/{id:.+}/{userId:,+}/{status:.+}")
    public Notice examine(@PathVariable int id, @PathVariable String userId, @PathVariable int status) {
        return noticeService.examine(userId, status, id);
    }

    @RequestMapping(method = RequestMethod.DELETE, value = "/{id:.+}")
    public void deleteNotice(@PathVariable int id) {
        noticeService.delete(id);
    }
}
