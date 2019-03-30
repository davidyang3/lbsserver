package com.tjufe.graduate.lbsserver.Controller;

import com.tjufe.graduate.lbsserver.Bean.Activity;
import com.tjufe.graduate.lbsserver.Bean.ActivityDetail;
import com.tjufe.graduate.lbsserver.Bean.ActivityImage;
import com.tjufe.graduate.lbsserver.Model.Pager;
import com.tjufe.graduate.lbsserver.Service.ActivityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("/LBS/activity")
public class ActivityController {

    @Autowired
    ActivityService activityService;

    @ResponseBody
    @GetMapping(value = "/list")
    public List<ActivityDetail> getList() {
        return activityService.getActivityList();
    }

    @ResponseBody
    @GetMapping(value = "/list/inTime")
    public List<ActivityDetail> getInTimeList() {
        return activityService.getInTimeList();
    }

    @ResponseBody
    @PostMapping(value = "/list/pager/{userId:.+}")
    public List<ActivityDetail> getWithPager(@PathVariable String userId, @RequestParam Pager pager) {
        return activityService.getActivityListByUserId(userId, pager.getStart(), pager.getEnd());
    }

    @ResponseBody
    @GetMapping(value = "/list/range/{longitude:.+}/{latitude:.+}/{radius:.+}")
    public List<ActivityDetail> getWithPager(@PathVariable double longitude, @PathVariable double latitude,
                                       @PathVariable double radius) {
        return activityService.getActivityInRange(longitude, latitude, radius);
    }

    @ResponseBody
    @RequestMapping(method = RequestMethod.PUT)
    public Activity createActivity(@RequestParam Activity activity) {
        return activityService.createActivity(activity);
    }

    @ResponseBody
    @PostMapping(value = "/update/title/{id:.+}/{title:.+}")
    public Activity updateTitle(@PathVariable int id, @PathVariable String title) {
        return activityService.updateTitle(id, title);
    }

    @ResponseBody
    @PostMapping(value = "/update/content/{id:.+}/{content:.+}")
    public Activity updateContent(@PathVariable int id, @PathVariable String content) {
        return activityService.updateContent(id, content);
    }

    @ResponseBody
    @PostMapping(value = "/update/adminId/{id:.+}/{adminId:.+}")
    public Activity updateAdminId(@PathVariable int id, @PathVariable int adminId) {
        return activityService.updateAdminId(id, adminId);
    }

    @ResponseBody
    @PostMapping(value = "/update/picture/{id:.+}/{picture:.+}")
    public Activity updatePicture(@PathVariable int id, @PathVariable String picture) {
        return activityService.updatePicture(id, picture);
    }

    @ResponseBody
    @PostMapping(value = "/update/startTime/{id:.+}/{startTime:.+}")
    public Activity updateStartTime(@PathVariable int id, @PathVariable Date startTime) {
        return activityService.updateStartTime(id, startTime);
    }

    @ResponseBody
    @PostMapping(value = "/update/endTime/{id:.+}/{endTime:.+}")
    public Activity updateEndTime(@PathVariable int id, @PathVariable Date endTime) {
        return activityService.updateEndTime(id, endTime);
    }

    @ResponseBody
    @PostMapping(value = "/update/status/{id:.+}/{status:.+}")
    public Activity updateStatus(@PathVariable int id, @PathVariable int status) {
        return activityService.updateStatus(id, status);
    }

    @ResponseBody
    @PostMapping(value = "/update/tag/{id:.+}")
    public List<Integer> updateTagList(@PathVariable int id, @RequestBody List<Integer> tagList) {
        return activityService.updateTagList(id, tagList);
    }

    @ResponseBody
    @PostMapping(value = "/update/image/{id:.+}")
    public List<ActivityImage> updateImageList(@PathVariable int id, @RequestBody List<String> imageList) {
        return activityService.updateActivityImage(id, imageList);
    }


    @ResponseBody
    @PostMapping(value = "/examine/{id:.+}/{userId:,+}/{status:.+}")
    public Activity examine(@PathVariable int id, @PathVariable String userId, @PathVariable int status) {
        return activityService.examine(id, userId, status);
    }

    @RequestMapping(method = RequestMethod.DELETE, value = "/{id:.+}")
    public void deleteActivity(@PathVariable int id) {
        activityService.deleteActivity(id);
    }

}
