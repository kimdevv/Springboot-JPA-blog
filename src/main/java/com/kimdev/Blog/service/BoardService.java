package com.kimdev.Blog.service;

import com.kimdev.Blog.dto.ReplySaveRequestDto;
import com.kimdev.Blog.model.Board;
import com.kimdev.Blog.model.Reply;
import com.kimdev.Blog.model.RoleType;
import com.kimdev.Blog.model.User;
import com.kimdev.Blog.repository.BoardRepository;
import com.kimdev.Blog.repository.ReplyRepository;
import com.kimdev.Blog.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service // 스프링이 컴포넌트 스캔을 통해 Bean에 등록을 해줌(IoC를 해줌).
public class BoardService {

    @Autowired
    private BoardRepository boardRepository;

    @Autowired
    private ReplyRepository replyRepository;

    @Autowired
    private UserRepository userRepository;

    @Transactional
    public void 글쓰기(Board board, User user) {
        try {
            board.setCount(0);
            board.setUser(user);
            boardRepository.save(board);
        } catch (Exception e) { // 등록 중 예외가 발생한다면
            e.printStackTrace();
            System.out.println("UserService : 글쓰기() : " + e.getMessage());
        }
    }

    @Transactional(readOnly = true) // SELECT만 하는 거니까 readOnly 해준다
    public List<Board> 글목록() {
        return boardRepository.findAll();
    }

    @Transactional(readOnly = true) // SELECT만 하는 거니까 readOnly 해준다
    public Board 글상세보기(int id) {
        return boardRepository.findById(id)
                .orElseThrow(() -> {
                    return new IllegalArgumentException("글 상세보기 실패 : 아이디를 찾을 수 없습니다.");
                });
    }

    @Transactional
    public void 글삭제하기(int id) {
        boardRepository.deleteById(id);
    }

    @Transactional
    public void 글수정하기(int id, Board requestBoard) {
        Board board = boardRepository.findById(id)
                .orElseThrow(() -> {
                    return new IllegalArgumentException("글 찾기 실패 : 아이디를 찾을 수 없습니다.");
                });
        board.setTitle(requestBoard.getTitle());
        board.setContent(requestBoard.getContent());
        // 해당 함수로 종료 시(Service가 종료될 때) 트랜잭션이 종료된다.
        // 이 때 더티 체킹! -> 자동 업데이트가 되면서 DB FLUSH(COMMIT).
    }

    @Transactional
    public void 댓글쓰기(ReplySaveRequestDto replySaveRequestDto) {
        User user = userRepository.findById(replySaveRequestDto.getUserId()).orElseThrow(() -> {
            return new IllegalArgumentException("댓글 쓰기 실패: 유저 id를 찾을 수 없습니다.");
        });

        Board board = boardRepository.findById(replySaveRequestDto.getBoardId()).orElseThrow(() -> {
            return new IllegalArgumentException("댓글 쓰기 실패: 게시글 id를 찾을 수 없습니다.");
        });

        Reply reply = new Reply();
        reply.update(user, board, replySaveRequestDto.getContent());

        replyRepository.save(reply);
    }

    @Transactional
    public void 댓글삭제(int replyId) {
        replyRepository.deleteById(replyId);
    }
}
