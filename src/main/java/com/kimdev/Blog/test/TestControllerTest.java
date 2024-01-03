package com.kimdev.Blog.test;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller // 이 어노테이션이 붙어있으면 파일을 반환한다!
public class TestControllerTest {
    @GetMapping("/temp/jsp")
    public String tempJsp() {
        return "/test"; // WEB-INF/vies/test.jsp를 찾는다.
    }

}
