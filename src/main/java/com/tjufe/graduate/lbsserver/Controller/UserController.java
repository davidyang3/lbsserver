package com.tjufe.graduate.lbsserver.Controller;

import com.tjufe.graduate.lbsserver.Bean.User;
import com.tjufe.graduate.lbsserver.Model.LogInResponse;
import com.tjufe.graduate.lbsserver.Service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "/LBS/user")
public class UserController {

    @Autowired
    UserService userService;

    /**
     * todo: can not transmit password with clear text, encrypt it
     * @param userId
     * @param password
     * @return
     */
    @ResponseBody
    @PostMapping("/logIn/{userId:.+}/{password:.+}")
    public LogInResponse logIn(@PathVariable String userId, @PathVariable String password) {
        return userService.logIn(userId, password);
    }

    @ResponseBody
    @GetMapping(value = "/userId/{userId:.+}")
    public User query(@PathVariable String userId) {
        return userService.queryWithId(userId);
    }

    /**
     * todo: can not transmit password with clear text, encrypt it
     * @param user
     * @return
     */
    @ResponseBody
    @PutMapping("/register")
    public User register(@RequestBody User user) {
        return userService.register(user);
    }

    @DeleteMapping("/{userId:.+}")
    public void deleteUser(@PathVariable String userId) {
        userService.deleteUser(userId);
    }

    @ResponseBody
    @PostMapping("/update/tel/{userId:.+}/{telNum:.+}")
    public String updateTelNum(@PathVariable String userId, @PathVariable String telNum) {
        return userService.updateTelNum(userId, telNum);
    }

    @ResponseBody
    @PostMapping("/update/email/{userId:.+}/{email:.+}")
    public String updateEmail(@PathVariable String userId, @PathVariable String email) {
        return userService.updateEmail(userId, email);
    }

    @ResponseBody
    @PostMapping("/update/portrait/{userId:.+}/{portrait:.+}")
    public String updatePortraitPath(@PathVariable String userId, @PathVariable String portrait) {
        return userService.updatePortraitPath(userId, portrait);
    }

    @ResponseBody
    @PostMapping("/update/status/{userId:.+}/{email:.+}")
    public int updateStatus(@PathVariable String userId, @PathVariable int status) {
        return userService.updateStatus(userId, status);
    }

    /**
     * todo: can not transmit password with clear text, encrypt it
     * @param userId
     * @param password
     * @param oldPassword
     * @return
     */
    @ResponseBody
    @PostMapping("/update/password/{userId:.+}/{password:.+}/{oldPassword:.+}")
    public User updatePassword(@PathVariable String userId, @PathVariable String password,
                               @PathVariable String oldPassword){
        return userService.updatePassword(userId, password, oldPassword);
    }

    @ResponseBody
    @PostMapping("/update/hobby/{userId:.+}")
    public List<Integer> updateHobby(@PathVariable String userId, @RequestBody List<Integer> hobbies) {
        return userService.updateHobbyList(userId, hobbies);
    }
}
