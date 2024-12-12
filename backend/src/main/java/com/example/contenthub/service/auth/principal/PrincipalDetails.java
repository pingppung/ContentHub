package com.example.contenthub.service.auth.principal;

import java.util.ArrayList;
import java.util.Collection;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.example.contenthub.entity.User;

import lombok.Data;

//시큐리티가 /auth/login 주소 요청이 오면 낚아채서 로그인 진행
//로그인 진행이 완료가 되면 시큐리티 session을 만들어줌(Secuirty ContextHolder)
//오브젝트 타입 => Authentication 타입 객체
//Authentication 안에 User 정보가 있어야 됨.
//User오브젝트 타입 => userDetails 타입 객체

//Security Session => Authentication => UserDetails(PrincipalDetails)
@Data
public class PrincipalDetails implements UserDetails {

    private User user;

    public PrincipalDetails(User user) {
        this.user = user;
    }

    // 해당 User의 권한을 리턴하는 곳
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        Collection<GrantedAuthority> authorities = new ArrayList<>();
        user.getRoles().forEach(r -> {
            authorities.add(() -> r.name());
        });
        return authorities;
    }

    @Override
    public String getPassword() {
        return user.getPassword();
    }

    @Override
    public String getUsername() {
        return user.getUsername();
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

}
