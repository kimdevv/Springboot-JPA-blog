package com.kimdev.Blog.test;

import lombok.*;


// Lombok의 @Getter과 @Setter 어노테이션을 쓰면 get, set 함수 굳이 안 만들어도 사용 가능!
//@Getter
//@Setter

@Data // 근데 @Getter이랑 @Setter 동시에 하고 싶으면 @Data 쓰면 됨!
@AllArgsConstructor // 생성자 자동으로 만들어줌.
@NoArgsConstructor // 빈 생성자?
public class Member {

    // 객체 지향에서는 변수는 private로 생성.
    // 변수 값 변경을 위해서는 메서드를 사용!
    // DB에 있는 값을 한 번 가져와서 저장하는 것이므로 final로 선언하는 게 좋음 (불변성 유지)
    private int id;
    private String username;
    private String password;
    private String email;
}
