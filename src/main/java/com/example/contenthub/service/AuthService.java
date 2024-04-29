package com.example.contenthub.service;

import org.springframework.stereotype.Service;

import com.example.contenthub.entity.User;
import com.example.contenthub.exception.UserException;
import com.example.contenthub.repository.UserRepository;
@Service
public class AuthService {

    private final UserRepository userRepository;

    public AuthService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public void saveUser(User user) throws UserException{
        User existingUser = userRepository.findByUserName(user.getUserName());
        if (existingUser != null) {
            // 이미 존재하는 사용자인 경우 예외 발생
            throw UserException.duplicateUserException();
        }
        userRepository.save(user);
    }
    
    public void loginUser(User user) throws UserException{
        User exisitUser = userRepository.findByUserNameAndUserPwd(user.getUserName(), user.getUserPwd());
        if(exisitUser == null){
            throw UserException.invalidUserException();
        }
    }

}