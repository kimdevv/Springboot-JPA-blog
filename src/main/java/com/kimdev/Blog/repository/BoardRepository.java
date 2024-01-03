package com.kimdev.Blog.repository;

import com.kimdev.Blog.model.Board;
import com.kimdev.Blog.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

// 자동으로 bean 등록이 됨 -> @Repository 생략 가능.
public interface BoardRepository extends JpaRepository<Board, Integer> {
}
