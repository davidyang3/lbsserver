package com.tjufe.graduate.lbsserver.Service;

import com.tjufe.graduate.lbsserver.Bean.Hobby;
import com.tjufe.graduate.lbsserver.Bean.User;
import com.tjufe.graduate.lbsserver.Dao.HobbyDao;
import com.tjufe.graduate.lbsserver.Dao.UserDao;
import com.tjufe.graduate.lbsserver.Model.LogInResponse;
import lombok.extern.slf4j.Slf4j;
import org.assertj.core.util.Lists;
import org.assertj.core.util.Sets;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.DigestUtils;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@Slf4j
public class UserService {

    @Autowired
    UserDao userDao;

    @Autowired
    HobbyDao hobbyDao;

    @Transactional
    Optional<User> getUserWithHobby(String userId) {
        Optional<User> userOptional = userDao.findById(userId);
        if (userOptional.isPresent()) {
            User user = userOptional.get();
            List<Hobby> hobbyList = hobbyDao.findByUserId(userId);
            List<Integer> hobbies = hobbyList.stream().map(hobby -> hobby.getHobbyId()).collect(Collectors.toList());
            user.setHobbyList(hobbies);
            return Optional.of(user);
        } else {
            return userOptional;
        }
    }

    /**
     * user log in
     * @param userId
     * @param password
     * @return  user=> user
     *           status=> 1: success; 0: password error; -1: user not exist
     */
    @Transactional
    public LogInResponse logIn(String userId, String password) {
        Optional<User> userOptional = getUserWithHobby(userId);
        int status = 0;
        if (userOptional.isPresent()) {
            User user = userOptional.get();
            String key = DigestUtils.md5DigestAsHex((userId + ":" + password).getBytes());
            if (key.equalsIgnoreCase(user.getPassword())) {
                //login success
                log.debug("user:{} log in success", userId);
                status = 1;
            } else {
                //password error
                log.error("user: {} log in error with password: {}", userId, password);
                status = -1;
            }
        } else {
            log.error("user: {} not exist", userId);
            status = -1;
        }
        LogInResponse response = new LogInResponse(userOptional.isPresent() ? userOptional.get() : null, status);
        return response;
    }

    public User queryWithId(String userId) {
        Optional<User> userOptional = userDao.findById(userId);
        if (userOptional.isPresent()) {
            User user = userOptional.get();
            user.setPassword(null);
            return user;
        } else {
            return null;
        }
    }

    /**
     * user register
     * @param user
     * @return user
     */
    @Transactional
    public User register(User user) {
        // todo : check validity
        String text = user.getUserId() + ":" +user.getPassword();
        String key = DigestUtils.md5DigestAsHex(text.getBytes());
        user.setPassword(key);
        userDao.save(user);
        List<Integer> hobbies = user.getHobbyList();
        if (hobbies != null) {
            hobbies.forEach(hobby -> hobbyDao.save(new Hobby(user.getUserId(), hobby)));
        }
        return user;
    }

    /**
     * todo : delete or set status
     * @param userId
     */
    public void deleteUser(String userId) {
        userDao.deleteById(userId);
    }

    @Transactional
    public String updateTelNum(String userId, String telNum) {
        Optional<User> userOptional = getUserWithHobby(userId);
        if (userOptional.isPresent()) {
            User user = userOptional.get();
            String old = user.getTelNumber();
            user.setTelNumber(telNum);
            // todo: check validity
            userDao.save(user);
            return old;
        } else {
            log.error("user: {} not exist", userId);
            return null;
        }
    }

    @Transactional
    public String updateNickName(String userId, String nickName) {
        Optional<User> userOptional = getUserWithHobby(userId);
        if (userOptional.isPresent()) {
            User user = userOptional.get();
            String old = user.getTelNumber();
            user.setNickName(nickName);
            // todo: check validity
            userDao.save(user);
            return old;
        } else {
            log.error("user: {} not exist", userId);
            return null;
        }
    }

    @Transactional
    public String updateEmail(String userId, String email) {
        Optional<User> userOptional = getUserWithHobby(userId);
        if (userOptional.isPresent()) {
            User user = userOptional.get();
            String old = user.getEmail();
            user.setEmail(email);
            // todo: check validity
            userDao.save(user);
            return old;
        } else {
            log.error("user: {} not exist", userId);
            return null;
        }
    }

    @Transactional
    public String updatePortraitPath(String userId, String portraitPath) {
        Optional<User> userOptional = getUserWithHobby(userId);
        if (userOptional.isPresent()) {
            User user = userOptional.get();
            String old = user.getPortraitPath();
            user.setPortraitPath(portraitPath);
            // todo: check validity
            userDao.save(user);
            return old;
        } else {
            log.error("user: {} not exist", userId);
            return null;
        }
    }

    @Transactional
    public int updateStatus(String userId, int status) {
        Optional<User> userOptional = getUserWithHobby(userId);
        if (userOptional.isPresent()) {
            User user = userOptional.get();
            int old = user.getStatus();
            user.setStatus(status);
            // todo: check validity
            userDao.save(user);
            return old;
        } else {
            log.error("user: {} not exist", userId);
            return -1;
        }
    }

    @Transactional
    public User updatePassword(String userId, String password, String oldPassword) {
        LogInResponse logInResponse = logIn(userId, oldPassword);
        if (logInResponse.getStatus() == 1) {
            User user = logInResponse.getUser();
            String key = DigestUtils.md5DigestAsHex((userId + ":" + password).getBytes());
            user.setPassword(key);
            // todo: check validity
            userDao.save(user);
            return user;
        } else {
            log.error("fail to update password with status: {}", logInResponse.getStatus());
            return null;
        }
    }

    @Transactional
    public List<Integer> updateHobbyList(String userId, List<Integer> hobbies) {
        Optional<User> userOptional = getUserWithHobby(userId);
        if (userOptional.isPresent()) {
            User user = userOptional.get();
            List<Integer> oldHobbies = user.getHobbyList();
            Set<Integer> set = Sets.newHashSet();
            set.addAll(hobbies);
            set.addAll(oldHobbies);
            List<Integer> toDelete = Lists.newArrayList();
            List<Integer> toAdd = Lists.newArrayList();
            set.forEach(hobby -> {
                if (oldHobbies.contains(hobby) && !hobbies.contains(hobby)) {
                    toDelete.add(hobby);
                } else if (!oldHobbies.contains(hobby) && hobbies.contains(hobby)) {
                    toAdd.add(hobby);
                }
            });
            toDelete.forEach(hobby -> hobbyDao.deleteAllByHobbyIdAndUserId(hobby, userId));
            toAdd.forEach(hobby -> hobbyDao.save(new Hobby(userId, hobby)));
            return oldHobbies;
        } else {
            log.error("user: {} not exist", userId);
            return Lists.newArrayList();
        }
    }
}
