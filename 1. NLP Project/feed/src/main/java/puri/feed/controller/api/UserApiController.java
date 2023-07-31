package puri.feed.controller.api;

import javax.servlet.http.HttpSession;

import org.springframework.http.HttpStatus;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import puri.feed.dto.ResponseDto;
import puri.feed.entity.User;
import puri.feed.service.UserService;

@Log4j2
@RestController
@RequiredArgsConstructor
public class UserApiController {
    private final UserService userService;

    // json 받음 = @RequestBody
    @PostMapping("api/user/save")
    public ResponseDto<Long> save(@RequestBody User user) {
        log.info("api user save() 호출");
        Long result = userService.join(user);
        log.info("UserApiController save() 호출");
        return new ResponseDto<Long>(HttpStatus.OK.value(), result); // user.js res 에 1리턴
    }

    @PostMapping("api/user/login")
    public ResponseDto<Integer> login(@RequestBody User user, HttpSession session, Model model) {
        log.info("api user login() 호출");

        Integer result = -1;
        String returnMsg = "";

        User m_user = userService.login(user); 

        if (m_user != null) {
            result = 1;
            session.setAttribute("user",m_user);
            returnMsg = "로그인 되었습니다. 환영합니다";
        }
        else {
            result = -1;
            session.setAttribute("user", null);
            returnMsg = "로그인 정보가 없습니다. 회원가입 해주세요^^";
        }
        log.info("session={}",session.getAttribute("user"));
        model.addAttribute("returnMsg", returnMsg);
        return new ResponseDto<Integer>(HttpStatus.OK.value(), result);
    }

    @PutMapping("api/user/update/{id}")
    public ResponseDto<Long> update(@PathVariable Long id, @RequestBody User user, HttpSession session, Model model) {
        log.info("api user update() 호출");

        Long result = 1L;
        result = userService.updateUser(id, user);
        return new ResponseDto<Long>(HttpStatus.OK.value(), result);
    }


    // 유저 삭제
    @DeleteMapping("api/user/delete/{id}")
    public ResponseDto<Long> deleteUser(HttpSession session, @PathVariable("id") String userid) {
        log.info("api user delete() 호출");

        Long result = 1L;
        userService.deleteUser(userid);
        return new ResponseDto<Long>(HttpStatus.OK.value(), result);
    }
    
}
