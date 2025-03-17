package com.example.contenthub.service.auth.principal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.example.contenthub.entity.User;
import com.example.contenthub.repository.UserRepository;

//시큐리티 설정에서 loginProcessUrl에서 발동
//로그인 요청이 오면 자동으로 userdetailsService 타입으로 IoC되어 있는 LoadUserByUsername 함수 실행
//http://localhost:3000/auth/login
@Service
public class PrincipalDetailsService implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User userEntity = userRepository.findByUserId(username);
        if (userEntity != null) {
            return new PrincipalDetails(userEntity);
        }
        throw new UsernameNotFoundException("User not exist with name :" + username);
    }

}
