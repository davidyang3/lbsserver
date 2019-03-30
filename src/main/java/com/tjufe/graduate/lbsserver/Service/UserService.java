package com.tjufe.graduate.lbsserver.Service;

import com.google.common.collect.ImmutableSet;
import com.tjufe.graduate.lbsserver.Bean.*;
import com.tjufe.graduate.lbsserver.Dao.*;
import com.tjufe.graduate.lbsserver.Model.LogInResponse;
import lombok.extern.slf4j.Slf4j;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.recipes.cache.PathChildrenCache;
import org.apache.zookeeper.CreateMode;
import org.assertj.core.util.Lists;
import org.assertj.core.util.Sets;
import org.redisson.api.RMap;
import org.redisson.api.RSet;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.DigestUtils;
import sun.misc.BASE64Decoder;

import javax.annotation.PostConstruct;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.*;
import java.util.stream.Collectors;

@Service
@Slf4j
public class UserService {

    private static final String LBS_REDIS_KEY_PREFIX = "com.tjufe.lbs";

    @Autowired
    UserDao userDao;

    @Autowired
    HobbyDao hobbyDao;

    @Autowired
    StudentDao studentDao;

    @Autowired
    ClassDao classDao;

    @Autowired
    MajorDao majorDao;

    @Autowired
    DeptDao deptDao;

    @Autowired
    StaffDao staffDao;

    @Autowired
    ShareTimeDao shareTimeDao;

    @Autowired
    CuratorFramework zkClient;

    @Autowired
    RedissonClient redissonClient;

    private String myUniqueTag = UUID.randomUUID().toString();

    private String localhost = getLocalhost();

    private static final String myPath = "/lbs/cache";

    private volatile ImmutableSet<String> activeHosts;

    private UserCache userCache;

    private final static String userImagePathPrefix = "com.tjufe.graduate.lbs.user.image.";

    @PostConstruct
    public void init() throws Exception {
        reloadUserCache();
        zkClient.create().creatingParentsIfNeeded().withMode(CreateMode.EPHEMERAL)
                .forPath(String.format("/lbs/cache/%s", myUniqueTag), localhost.getBytes());
        PathChildrenCache childrenCache = new PathChildrenCache(zkClient, myPath, false);
        childrenCache.start();
        childrenCache.getListenable().addListener((client, event) -> {
            ImmutableSet<String> newActiveHosts = reloadActiveHosts();
            log.debug("Hosts changed previous={} current={}", activeHosts, newActiveHosts);
            activeHosts = newActiveHosts;
        });
        activeHosts = reloadActiveHosts();
    }

    @Scheduled(fixedDelay = 2000, initialDelay = 1000)
    public void updateCache() {
        Map<String, String> hostChecksumMap = cachedHosts().getAll(activeHosts);
        Set<String> allVersions = new TreeSet<>(hostChecksumMap.values());
        switch (allVersions.size()) {
            case 1:
                log.debug("All hosts in same stat, continue");
                break;
            case 0:
                log.warn("Unknown stat for user cache");
                break;
            default:
                log.info("checksum does not equal in allVersions hosts, reload myself");
                reloadUserCache();
                break;
        }
    }

    @Scheduled(fixedDelay = 100)
    public void updateLocalDomainCache() {
        RSet<String> userUpdated = userUpdateSet(myUniqueTag);
        String userId = userUpdated.removeRandom();
        // TODO: 19/07/2017  可以合并操作后再一起更新checksum， 效率更高
        while (userId != null) {
            User user = userDao.findById(userId).get();
            if (user != null) {
                List<ShareTime> shareTimes = shareTimeDao.findByUserId(userId);
                log.debug("Before update, local user cache checksum={}", userCache.checksum);
                if (user != null) {
                    ShareTime shareTime;
                    if (shareTimes.size() > 0) {
                        shareTime = shareTimes.get(0);
                    } else {
                        shareTime = new ShareTime();
                    }
                    UserStatus userStatus = new UserStatus(userId, user.getStatus(),
                            shareTime.getStartTime(), shareTime.getEndTime());
                    userCache.updateUser(userStatus);
                    log.debug("User={} updated, update local cache, new checksum={}",
                            userId, userCache.checksum);
                } else {
                    userCache.deleteUser(userId);
                    log.debug("User={} has been deleted, remove from local cache, new checksum={}",
                            userId, userCache.checksum);
                }
                cachedHosts().fastPut(myUniqueTag, userCache.checksum);
            }
            userId = userUpdated.removeRandom();
        }
    }

    private RSet<String> userUpdateSet(String host) {
        return redissonClient.getSet(LBS_REDIS_KEY_PREFIX + "user." + host);
    }

    public void reloadUserCache() {
        List<UserStatus> userStatuses = Lists.newArrayList();
        List<User> list = userDao.findAll();
        list.forEach(user -> {
            List<ShareTime> shareTimes = shareTimeDao.findByUserId(user.getUserId());
            ShareTime shareTime;
            if (shareTimes.size() > 0) {
                shareTime = shareTimes.get(0);
            } else {
                shareTime = new ShareTime();
            }
            userStatuses.add(new UserStatus(user.getUserId(), user.getStatus(), shareTime.getStartTime(),
                    shareTime.getEndTime()));
        });
        userCache = new UserCache(userStatuses);
        cachedHosts().fastPut(myUniqueTag, userCache.checksum);
    }

    private RMap<String, String> cachedHosts() {
        return redissonClient.getMap(LBS_REDIS_KEY_PREFIX + "domain.hosts");
    }

    private String getLocalhost() {
        try {
            return InetAddress.getLocalHost().getHostName();
        } catch (UnknownHostException e) {
            return UUID.randomUUID().toString();
        }
    }

    public void broadcastUserUpdate(String userId) {
        activeHosts.parallelStream().forEach(host -> userUpdateSet(host).add(userId));
    }

    private ImmutableSet<String> reloadActiveHosts() throws Exception {
        return ImmutableSet.copyOf(zkClient.getChildren().forPath(myPath));
    }

    public UserDetail handleUser(User user) {
        UserDetail userDetail = new UserDetail(user);
        if (user.isStudent()) {
            Student student = studentDao.getOne(user.getUserId());
            _Class _class = classDao.getOne(student.getClassId());
            Major major = majorDao.getOne(_class.getMajorId());
            Dept dept = deptDao.getOne(major.getDeptId());
            userDetail.setClassName(_class.getClassName());
            userDetail.setMajorName(major.getMajorName());
            userDetail.setDeptName(dept.getDeptName());
        } else {
            Staff staff = staffDao.getOne(user.getUserId());
            if (staff != null) {
                userDetail.setDepartmentId(staff.getDepartmentId());
                userDetail.setPosition(staff.getPosition());
            }
        }
        return userDetail;
    }

    public UserStatus getUserStatus(String userId) {
        return userCache.getUserStatus(userId);
    }

    @Transactional
    Optional<UserDetail> getUserWithHobby(String userId) {
        Optional<User> userOptional = userDao.findById(userId);
        if (userOptional.isPresent()) {
            User user = userOptional.get();
            List<Hobby> hobbyList = hobbyDao.findByUserId(userId);
            List<Integer> hobbies = hobbyList.stream().map(hobby -> hobby.getHobbyId()).collect(Collectors.toList());
            user.setHobbyList(hobbies);
            return Optional.of(handleUser(user));
        } else {
            return Optional.empty();
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
        Optional<User> userOptional = userDao.findById(userId);
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

    public UserDetail queryWithId(String userId) {
        Optional<User> userOptional = userDao.findById(userId);
        if (userOptional.isPresent()) {
            User user = userOptional.get();
            user.setPassword(null);
            return handleUser(user);
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
        Optional<User> userOptional = userDao.findById(userId);
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
    public String updateImage(String userId, String image) {
        Optional<User> userOptional = userDao.findById(userId);
        StringBuilder stringBuilder = new StringBuilder();
        String imagePath = stringBuilder.append(userImagePathPrefix).append(userId).toString();
        redissonClient.getBucket(imagePath).set(image);
        log.debug("save user image in {}", imagePath);
        if (userOptional.isPresent()) {
            User user = userOptional.get();
            String old = user.getUserImage();
            user.setUserImage(imagePath);
            // todo: check validity
            userDao.save(user);
            return old;
        } else {
            log.error("user: {} not exist", userId);
            return null;
        }
    }

    public static boolean generateImage(String imgStr, String imagePath)
    {   //对字节数组字符串进行Base64解码并生成图片
        if (imgStr == null) //图像数据为空
            return false;
        BASE64Decoder decoder = new BASE64Decoder();
        try
        {
            //Base64解码
            byte[] b = decoder.decodeBuffer(imgStr);
            for(int i=0;i<b.length;++i)
            {
                if(b[i]<0)
                {//调整异常数据
                    b[i]+=256;
                }
            }
            //生成jpeg图片
            OutputStream out = new FileOutputStream(imagePath);
            out.write(b);
            out.flush();
            out.close();
            return true;
        }
        catch (Exception e)
        {
            log.error("========{}", e.getMessage());
            return false;
        }
    }

    @Transactional
    public String updateNickName(String userId, String nickName) {
        Optional<User> userOptional = userDao.findById(userId);
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
    public void updateShareTime(String userId, long start, long end) {
        shareTimeDao.findByUserId(userId).forEach(shareTime -> {
            shareTime.setEndTime(end);
            shareTime.setStartTime(start);
            shareTimeDao.save(shareTime);
            broadcastUserUpdate(userId);
        });
    }

    @Transactional
    public String updateEmail(String userId, String email) {
        Optional<User> userOptional = userDao.findById(userId);
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
        Optional<User> userOptional = userDao.findById(userId);
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
        Optional<User> userOptional = userDao.findById(userId);
        if (userOptional.isPresent()) {
            User user = userOptional.get();
            int old = user.getStatus();
            user.setStatus(status);
            // todo: check validity
            userDao.save(user);
            broadcastUserUpdate(userId);
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
        Optional<UserDetail> userOptional = getUserWithHobby(userId);
        if (userOptional.isPresent()) {
            UserDetail user = userOptional.get();
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
