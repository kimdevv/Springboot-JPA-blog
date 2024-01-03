package com.kimdev.Blog.repository;

import com.kimdev.Blog.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

// 자동으로 bean 등록이 됨 -> @Repository 생략 가능.
public interface UserRepository extends JpaRepository<User, Integer> { // 해당 Repository는 User을 관리하며, PK는 Integer이다.
    /*// JPA Naming 쿼리 -> findByXXX로 이름 지으면 JPA가 자동으로 XXX로 SELECT 해줌!
    // SELECT * FROM user WHERE username = ? AND password = ?
    User findByUsernameAndPassword(String username, String password);

    // 또는 이렇게 짜도 똑같은 역할을 함!
    @Query(value = "SELECT * FROM user WHERE username = ? AND password = ?", nativeQuery = true)
    User login(String username, String password);*/

    Optional<User> findByUsername(String username);
}
