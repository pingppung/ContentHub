package com.example.contenthub.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.example.contenthub.dto.JwtDto;
import com.example.contenthub.entity.User;
import com.example.contenthub.exception.UserException;
import com.example.contenthub.repository.UserRepository;

import lombok.RequiredArgsConstructor;


//인증 정보를 처리하고, 인증이 성공하면 JwtService를 통해 JWT를 생성
@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    @Autowired
    private JwtService jwtService;


    public void saveUser(User request) throws UserException{
        User existingUser = userRepository.findByUserName(request.getUserName());
        if (existingUser != null) {
            // 이미 존재하는 사용자인 경우 예외 발생
            throw UserException.duplicateUserException();
        }
        User user = User.builder().userName(request.getUserName()).userPwd(passwordEncoder.encode(request.getUserPwd())).build();
        userRepository.save(user);
        //System.out.println(user.getId());
    }
    
    public JwtDto loginUser(User request) throws UserException{
        User user = userRepository.findByUserName(request.getUserName());
        if (user == null || !passwordEncoder.matches(request.getUserPwd(), user.getUserPwd())) {
            throw UserException.invalidUserException();
        }
        String token = jwtService.generateToken(user.getId(), user.getUserName());
        return new JwtDto(token);
    }

}