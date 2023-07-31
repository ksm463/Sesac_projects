package puri.feed.controller;

import java.util.List;

import org.springframework.boot.autoconfigure.data.web.SpringDataWebProperties.Pageable;
import org.springframework.data.domain.Page;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.*;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import puri.feed.entity.RoleType;
import puri.feed.entity.User;
import puri.feed.repository.UserRepository;
import org.springframework.data.domain.*;

@Log4j2
@RequiredArgsConstructor  
@RestController
public class UserTestController {
    private final UserRepository userRepository;

    @PostMapping("user/join2")
    public String join2(@RequestParam("userid") String userid,
                        @RequestParam("username") String username,
                       @RequestParam("password") String password,
                       @RequestParam("email") String email) {
        User user = new User();
        user.setUsername(username);
        user.setPassword(password);
        user.setEmail(email);
        user.setRole(RoleType.USER);
        userRepository.save(user);
        return "post join1 테스트";
    }
    
    @GetMapping("user/detail/{id}")
    public User detail(@PathVariable Long id) {
        User user = userRepository.findById(id).orElseThrow(()->{
            return new IllegalArgumentException("존재하지 않는 유저입니다 : " + id);
        });
        return user;  
    }   
    
    @GetMapping("/user/users")
    public List<User> userList() {
        return userRepository.findAll();
    }

    // @GetMapping()
    // public List<User> pageList(@PageableDefault(size = 2, sort = "id", direction = Sort.Direction.DESC) Pageable pageable) {
    //     Page<User> pagingUser = userRepository.findAll(pageable);

    //     List<User> users = pagingUser.getContent();
    //     return users;
    // }
}
