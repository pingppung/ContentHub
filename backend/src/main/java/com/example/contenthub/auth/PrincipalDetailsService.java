package com.example.contenthub.auth;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.example.contenthub.user.User;
import com.example.contenthub.user.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class PrincipalDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username)
            throws UsernameNotFoundException {

        User user = userRepository.findByUserId(username)
                .orElseThrow(() ->
            new UsernameNotFoundException("사용자를 찾을 수 없습니다. userId=" + username )
        );

        return new PrincipalDetails(user);
    }

}
