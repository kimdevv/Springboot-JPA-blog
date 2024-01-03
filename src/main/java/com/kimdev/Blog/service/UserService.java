package com.kimdev.Blog.service;

import com.kimdev.Blog.model.RoleType;
import com.kimdev.Blog.model.User;
import com.kimdev.Blog.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import org.springframework.transaction.annotation.Transactional;

@Service // 스프링이 컴포넌트 스캔을 통해 Bean에 등록을 해줌(IoC를 해줌).
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private BCryptPasswordEncoder encoder;

    @Transactional(readOnly = true)
    public User 회원찾기(String username) {
        User user = userRepository.findByUsername(username).orElseGet(()-> {
            return new User();
                });
        return user;
    }

    @Transactional
    public int 회원가입(User user) {
        try {
            String rawPassword = user.getPassword(); // 원래 비밀번호
            String encPassword = encoder.encode(rawPassword); // 해쉬화된 비밀번호
            user.setPassword(encPassword);
            user.setRole(RoleType.USER);
            userRepository.save(user);
            return 1;
        } catch (Exception e) { // 등록 중 예외가 발생한다면
            e.printStackTrace();
            System.out.println("UserService : 회원가입() : " + e.getMessage());
        }
        return -1;
    }

    @Transactional
    public void 회원수정(User user) {
        // 수정 시에는 영속성 컨텍스트 User 오브젝트를 영속화시키고, 영속화된 User 오브젝트를 수정.
        // SELECT를 해서 User오브젝트를 DB로부터 영속화 컨텍스트로 이동.
        // 이후 영속화된 오브젝트를 변경하면 자동으로 DB에 UPDATE문을 날려준다!
        User persistance = userRepository.findById(user.getId()).orElseThrow(() -> {
            return new IllegalArgumentException("회원 찾기 실패");
        });

        // OAuth(카카오 등) 사용자가 아닌 경우에만 패스워드 수정 가능
        if(persistance.getOauth() == null || persistance.getOauth().equals("")) {
            String rawPassword = user.getPassword();
            String encPassword = encoder.encode(rawPassword);
            persistance.setPassword(encPassword);
            persistance.setEmail(user.getEmail());
        }

        // 회원 수정 함수 종료 = 서비스 종료 = 트랜잭션 종료 -> COMMIT 자동 수행
        // 즉 영속화된 persistance 객체의 변화가 감지되면(더티 체킹), UPDATE문을 자동으로 날려줌.
    }

    /*@Transactional(readOnly = true) // SELECT할 때 트랜잭션 시작. 서비스가 종료될 때 트랜잭션 종료. 여기까지 정합성 유지!
    public User 로그인(User user) {
        return userRepository.findByUsernameAndPassword(user.getUsername(), user.getPassword());
    }*/

}
