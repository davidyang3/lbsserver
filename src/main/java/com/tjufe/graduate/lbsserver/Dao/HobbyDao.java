package com.tjufe.graduate.lbsserver.Dao;

import com.tjufe.graduate.lbsserver.Bean.Hobby;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface HobbyDao extends JpaRepository<Hobby, Integer> {

    List<Hobby> findByUserId(String userId);

    @Modifying
    @Query(value = "delete from hobby where hobby_id =?1 and user_id =?2", nativeQuery = true)
    int deleteAllByHobbyIdAndUserId(int hobbyId, String userId);
}
