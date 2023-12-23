package com.kimdev.Blog.test;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;


// 이 어노테이션을 쓰면, 스프링이 com.kimdev.blog 패키지 이하를 스캔해서 모든 하일을 메모리에 new 하는 게 아니라,
// 특정 어노테이션이 붙어있는 클래스 파일들만 new해서(IoC) 스프링 컨테이너로 관리..
@RestController
public class BlogControllerTest {

    // http://localhost:8080/test/hello
    @GetMapping("/test/hello")
    public String hello() {
        return "<h1>hello spring boot</h1>";
    }
}
