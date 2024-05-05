package com.example.contenthub.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.contenthub.entity.User;
import java.util.List;


@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    User findByUserName(String userName);
    User findByUserNameAndUserPwd(String userName, String userPwd);
}
