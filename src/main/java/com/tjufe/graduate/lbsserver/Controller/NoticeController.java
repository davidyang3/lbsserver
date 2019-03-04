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
    @GetMapping(value = "/list/pager/{userId:.+}")
    public List<NoticeDetail> getWithPager(@PathVariable String userId, @RequestParam Pager pager) {
        return noticeService.getRecommandNotice(userId, pager.getStart(), pager.getEnd());
    }

    @ResponseBody
    @RequestMapping(method = RequestMethod.PUT)
    public Notice create(@RequestParam Notice notice) {
        return noticeService.create(notice);
    }

    @ResponseBody
    @PostMapping(value = "/update/title/{id:.+}/{title:.+}")
    public Notice updateTitle(@PathVariable int id, @PathVariable String title) {
        return noticeService.updateTitle(id, title);
    }

    @ResponseBody
    @PostMapping(value = "/update/content/{id:.+}/{content:.+}")
    public Notice updateContent(@PathVariable int id, @PathVariable String content) {
        return noticeService.updateContent(id, content);
    }

    @ResponseBody
    @PostMapping(value = "/update/adminId/{id:.+}/{adminId:.+}")
    public Notice updateAdminId(@PathVariable int id, @PathVariable int adminId) {
        return noticeService.updateAdminId(id, adminId);
    }

    @ResponseBody
    @PostMapping(value = "/update/picturePath/{id:.+}/{picturePath:.+}")
    public Notice updatePicturePath(@PathVariable int id, @PathVariable String picturePath) {
        return noticeService.updatePicturePath(id, picturePath);
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
    public List<NoticeImage> updateImageList(@PathVariable int id, @RequestBody List<NoticeImage> imageList) {
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