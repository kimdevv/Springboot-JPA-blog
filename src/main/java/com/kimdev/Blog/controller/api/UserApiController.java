package com.kimdev.Blog.controller.api;

import com.kimdev.Blog.config.auth.PrincipalDetail;
import com.kimdev.Blog.dto.ResponseDto;
import com.kimdev.Blog.model.RoleType;
import com.kimdev.Blog.model.User;
import com.kimdev.Blog.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpSession;

@RestController
public class UserApiController {

    // @Autowired 하면 이 객체들은 스프링 컨테이너가 Bean으로 등록해둔 거 그냥 꺼내서 쓸 수 있음 아무 곳에서나.
    @Autowired
    private UserService userService;

    @Autowired
    private AuthenticationManager authenticationManager;

    //@Autowired
    //private HttpSession session;

    @PostMapping("/auth/joinProc")
    public ResponseDto<Integer> save(@RequestBody User user) {
        System.out.println("UserApiController : save 호출됨");

        // 실제로 DB에 insert하는 부분
        int result = userService.회원가입(user); // 1이면 성공, -1이면 실패.
        return new ResponseDto<Integer>(HttpStatus.OK.value(), result);
    }

    @PutMapping("/user")
    public ResponseDto<Integer> update(@RequestBody User user) { // JSON으로 받으려면 @RequestBody
        userService.회원수정(user);

        // 세션 등록
        Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(user.getUsername(), user.getPassword()));
        SecurityContextHolder.getContext().setAuthentication(authentication);

        return new ResponseDto<Integer>(HttpStatus.OK.value(), 1);
    }

    /*@PostMapping("/api/user/login")
    public ResponseDto<Integer> login(@RequestBody User user) {
        System.out.println("UserApiController : login 호출됨");
        User principal = userService.로그인(user); // principal은 접근주체 라는 뜻.

        if(principal != null) {
            session.setAttribute("principal", principal); // 받은 User으로 세션을 만듦.
        }

        return new ResponseDto<Integer>(HttpStatus.OK.value(), 1);
    }*/
}
