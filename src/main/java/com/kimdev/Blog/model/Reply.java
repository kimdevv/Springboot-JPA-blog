package com.kimdev.Blog.model;

import com.kimdev.Blog.dto.ReplySaveRequestDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import java.sql.Timestamp;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
@Entity
public class Reply {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(nullable = false, length = 200)
    private String content;

    @ManyToOne // 여러 개의 답변은 하나의 게시글에 존재 가능.
    @JoinColumn(name="boardId")
    private Board board;

    @ManyToOne // 여러 개의 답변을 하나의 유저가 작성 가능.
    @JoinColumn(name="userId")
    private User user;

    @CreationTimestamp
    private Timestamp createDate;

    public void update(User user, Board board, String content) {
        setUser(user);
        setBoard(board);
        setContent(content);
    }

}
