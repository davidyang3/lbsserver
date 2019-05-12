package com.tjufe.graduate.lbsserver.Dao;

import com.tjufe.graduate.lbsserver.Bean.User;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface UserDao extends JpaRepository<User, String> {

    List<User> findByNickName(String name);

    List<User> findByTypeAndNickName(int type, String name);
}
