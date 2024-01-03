package com.kimdev.Blog.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.List;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
@Entity
public class Board {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // auto_increment
    private int id;

    @Column(nullable = false, length = 100)
    private String title;

    @Lob // 대용량 데이터에 사용
    private String content; // 섬머노트 라이브러리 -> <html>태그가 섞여서 디자인이 돼서 용량 엄청 커짐.

    private int count; // 조회수

    @CreationTimestamp
    private Timestamp createDate;

    @ManyToOne // Board가 Many, User가 One -> 1명의 유저는 여러 개의 게시글을 쓸 수 있다는 것.
    @JoinColumn(name="userId") // 실제로 테이블이 만들어질 때엔 userId라는 이름으로 저장될 것!
    private User user; // DB는 오브젝트를 저장할 수 없기에 FK를 사용하지만, 자바에서는 오브젝트를 저장 가능.
    // 이렇게] 오브젝트로 선언해도, 자바는 DB에 맞게 (userId라고) 저장해준다.

    @OneToMany(mappedBy = "board", fetch = FetchType.EAGER, cascade = CascadeType.REMOVE) // 하나의 게시글에는 여러 개의 댓글이 올 수 있다. mappedBy는 '나는 연관관계의 주인이 아니다!'는 뜻 -> DB에 reply 칼럼을 만들지 않는다. (Reply의 board가 주인)
    // FetchType.EAGER 이거는 이거 가져올 때 이 board 값들 무조건 가져온다는 뜻! 게시글 보면 댓글들이 당연히 처음부터 떠 있어야 하니까!
    // ManyToOne은 .EAGER이 디폴트인데 OneToMany는 .LAZY가 디폴트라서 여기서는 이렇게 직접 선언해주는 것.
    //@JoinColumn(name="replyId") // 이게 필요 없다! 댓글이 여러 개인데 replyId 있으면 replyId가 여러 개 저장돼야 함... 원자성 깨짐! 이거 대신 mappedBy를 사용하는 것.
    @JsonIgnoreProperties({"board"}) // 무한 참조 방지
    @OrderBy("id desc") // id값으로 내림차순 정렬
    private List<Reply> replys;
    // 즉 DB에 컬럼을 만들지 않고, 단지 Board를 셀렉트할 떄 조인문을 통해 값을 얻기 위해 사용되는 것!
    // 그런데 반드시 댓글들을 가져와야 하므로 fetch를 EAGER로 바꿔주는 것!

}
