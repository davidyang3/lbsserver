package com.tjufe.graduate.lbsserver.Dao;

import com.tjufe.graduate.lbsserver.Bean.User;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface UserDao extends JpaRepository<User, String> {

    List<User> findByUserName(String name);

    List<User> findByTypeAndUserName(int type, String name);
}
