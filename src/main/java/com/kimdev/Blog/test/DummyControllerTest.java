package com.kimdev.Blog.test;

import com.kimdev.Blog.model.RoleType;
import com.kimdev.Blog.model.User;
import com.kimdev.Blog.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.*;

import javax.transaction.Transactional;
import java.util.List;
import java.util.function.Supplier;

@RestController
public class DummyControllerTest {

    @Autowired // DummyControllerTest가 메모리에 뜰 때, 이 userRepository도 같이 메모리에 뜸.
    // "의존성 주입(DI)"
    private UserRepository userRepository;

    @Transactional // 더티 체킹!(함수 종료 시 자동 commit) -> save()를 하지 않아도 update 가능!
    @PutMapping("/dummy/user/{id}")
    public User updateUser(@PathVariable int id, @RequestBody User requestUser) { // JSON 데이터를 요청 -> 자바 객체로 변환해서 받아줌.
        requestUser.setId(id);
        userRepository.save(requestUser); // 그 id를 가지고 DB 값 업데이트 해주는데... 주어지지 않은 값들이 null외 되는 한계가 있어서 잘 안 씀.

        User user = userRepository.findById(id).orElseThrow(() -> {
            return new IllegalArgumentException("수정에 실패하였습니다.");
        });
        user.setPassword(requestUser.getPassword());
        user.setEmail(requestUser.getEmail());

        requestUser.setId(id);
        //userRepository.save(requestUser); // 그 id를 가지고 DB 값 업데이트 해주는데... 주어지지 않은 값들이 null외 되는 한계가 있어서 잘 안 씀.
            // save 함수는 id를 전달하지 않으면 insert
            // save 함수는 id를 전달하면 해당 id에 대한 데이터가 있으면 update
            // save 함수는 id를 전달하면 해당 id에 대한 데이터가 없으면 insert
        return null;
    }

    @DeleteMapping("/dummy/user/{id}")
    public String delete(@PathVariable int id) {
        try {
            userRepository.deleteById(id);
        } catch (EmptyResultDataAccessException e) {
            return "삭제에 실패하였습니다. 해당 id는 DB에 없습니다.";
        }

        return "삭제되었습니다. id: " + id;
    }

    // {id} 주소로 파라미터를 전달받을 수 있음 -> http://localhost:8000/blog/dummy/user/3
    @GetMapping("/dummy/user/{id}")
    public User detail(@PathVariable int id) { // 저 {id}에 받을 값을 이 @PathVariable 어노테이션으로 처리한다.
        //userRepository.findById(id); // 이거 하면 Optional 객체를 반환함... User 객체가 아님! null인지 판단해서 받아올 수 있게 Optional로 반환하는 것.
        //userRepository.findById(id).get(); // 이건 바로 Optional에 감싸진 객체(User) 빼서 반환하는 건데... 없는 id를 입력하면 오류날 가능성!
        User user = userRepository.findById(id).orElseGet(new Supplier<User>() { // Supplier은 인터페이스 -> 얘가 가진 get() 추상 메서드를 오버라이드 해서 써줘야 함.
            @Override
            public User get() {
                return new User();
            }
        }); // 즉 이렇게 써주면! null이 아닌 경우에는 그 User 객체가 전달되고, null인 경우에는 새 User이 만들어져서 반환되는 것!

        // 근데 없는 유저는 반환이 안 돼야지... 막 만들어서 반환하면 우짬?
        // 그래서 이렇게 없는 id를 입력하면 오류를 던지도록 만들면 된다!
        user = userRepository.findById(id).orElseThrow(new Supplier<IllegalArgumentException>() {
            @Override
            public IllegalArgumentException get() {
                return new IllegalArgumentException("해당 유저는 없습니다. id: " + id);
            }
        });

        // 근데 웹브라우저에서 요청하는데 반환되는 건 자바 오브젝트(User)... -> 웹브라우저가 자바 객체는 이해하지 못한다!
        // 그래서 자바 객체를 웹브라우저가 이해할 수 있게 변환해줘야 하는데, 가장 좋은 게 JSON!
        // 스프링부트의 MessageConverter가 자동으로 Jackson 라이브러리를 호출시켜 JSON으로 변환해서 반환해주는 것!
        return user;
    }

    @GetMapping("/dummy/users")
    public List<User> list() {
        return userRepository.findAll();
    }

    // 한 페이지 당 2건의 데이터를 리턴받아 볼 예정.
    @GetMapping("/dummy/user")
    public List<User> pageList(@PageableDefault(size=2, sort="id", direction= Sort.Direction.DESC) Pageable pageable) {
        Page<User> pagingUser = userRepository.findAll(pageable);

        List<User> users = pagingUser.getContent(); // List를 반환한다.
        return users;
    }

    @PostMapping("/dummy/join") // 회원가입을 해야 하므로 post
    //public String join(String username, String password, String email) { // 변수명 정확히 적어주기만 한다면 @RequestParam 필요X
    public String join(User user) { // 그런데 자바에서는 이렇게 Object로 받을 수도 있음!
        System.out.println("username: " + user.getUsername());
        System.out.println("password: " + user.getPassword());
        System.out.println("email: " + user.getEmail());
        System.out.println("role: " + user.getRole());
        System.out.println("createDate: " + user.getCreateData());

        user.setRole(RoleType.USER); // 디폴트 값 설정
        userRepository.save(user);
        return "회원가입이 완료되었습니다.";
    }
}
