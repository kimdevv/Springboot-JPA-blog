package com.kimdev.Blog.test;

import org.springframework.web.bind.annotation.*;

// 사용자가 요청하면 응답(Data)해주는 컨트롤러.
// Data가 아니라 HTML 파일로 응답해주는 컨트롤러가 @Controller임.
@RestController
public class HttlpControllerTest {

    // 웹 브라우저에 주소 창으로 요청할 때에는 GET 요청 밖에 할 수 없다!
    // http://localhost:8080/http/get (select)
    @GetMapping("/http/get")
    public String getTest(Member m) { // Member 클래스를 만들어 놨으니 id나 username을 따로 안 가져와도 된다. 이 경우에는 @RequestParam 안 써도 됨!
    // public String getTest(@RequestParam int id, @RequestParam String username) { // @RequestParam은 주소 입력 시 마지막에 ?id=1 이런 식으로 매개변수 줄 수 있는 것.
        return "gets 요청 : " + m.getId() + " " + m.getUsername(); // getPassword()나 getEmail() 등도 되겠지? 매개변수 수에 따라!
    }

    // http://localhost:8080/http/post (insert)
    @PostMapping("/http/post")
    //public String postTest(@RequestBody String text) { // 주소에 매개변수 입력하는 게 아니라 Body를 써서 매개변수 주는 것도 가능. 이 경우 @RequestBody 어노테이션 사용!
    public String postTest(@RequestBody Member m) { // 근데 저렇게 하면 text로밖에 전송이 안 돼서... 이렇게 Member으로 받으면 JSON 형식으로 보내도 id username password email 다 구분해서 저장 가능!
        return "post 요청 : " + m.getId() + " " + m.getUsername();
    }

    // http://localhost:8080/http/put (update)
    @PutMapping("/http/put")
    public String putTest() {
        return "put 요청";
    }

    // http://localhost:8080/http/delete (delete)
    @DeleteMapping("/http/delete")
    public String deleteTest() {
        return "delete 요청";
    }
}
