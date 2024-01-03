package com.kimdev.Blog.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.DynamicInsert;

import javax.persistence.*;
import java.sql.Timestamp;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
@Entity // User 클래스가 테이블화 -> JPA를 통해 이 클래스를 가지고 자동으로 테이블이 생성됨.
//@DynamicInsert // insert 시 null인 필드는 제외한다 (role이 디폴트 값으로 들어감)
public class User {

    @Id // Primary key
    @GeneratedValue(strategy = GenerationType.IDENTITY) // numbering 전략이 해당 프로젝트와 연결된 DB의 numbering 전략을 따라간다!
    private int id;

    @Column(nullable = false, length = 30, unique = true) // null이 될 수 없고, 최대 길이 30자 제한
    private String username; // 아이디

    @Column(length = 100) // null이 될 수 없고, 최대 길이 100자 제한
    private String password; // 비밀번호

    @Column(nullable = false, length = 50) // null이 될 수 없고, 최대 길이 50자 제한
    private String email; // 이메일

    //@ColumnDefault("'user'") // Default값 설정
    @Enumerated(EnumType.STRING) // DB에는 RoleType이라는 타입이 없으므로, 이 Enum이 String 타입이라는 것을 알려주는 것.
    private RoleType role; // 도메인을 설정할 수 있는 Enum으로 쓰는 게 좋다. (String 보다는)

    private String oauth; // kakao, google, ...

    @CreationTimestamp // 시간이 자동으로 입력된다
    private Timestamp createData; // 생성 날짜
}
