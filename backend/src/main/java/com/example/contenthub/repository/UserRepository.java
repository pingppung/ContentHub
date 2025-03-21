package com.example.contenthub.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.contenthub.entity.User;
import java.util.List;

//CRUD를 JpaRepository가 들고 있음
@Repository
public interface UserRepository extends JpaRepository<User, Integer> {
    User findByUserId(String userId);

    User findByUserIdAndPassword(String userId, String password);
}
