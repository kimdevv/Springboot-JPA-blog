package com.kimdev.Blog.config.auth;

import com.kimdev.Blog.model.User;
import com.kimdev.Blog.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service // Bean에 등록
public class PrincipalDetailService implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    // 스프링이 로그인 요청을 가로챌 때, username, password를 가로채는데
    // password 부분 처리는 알아서 함.
    // -> 해당 username이 DB에 있는지만 확인해주면 됨. 그걸 이 함수에서 한다.
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User principal = userRepository.findByUsername(username)
                .orElseThrow(() ->{
                    return new UsernameNotFoundException("해당 사용자를 찾을 수 없습니다.: " + username);
                });
        return new PrincipalDetail(principal); // 시큐리티의 세션에 유저 정보가 저장이 됨.
    }
}
