package puri.feed.controller;

import java.util.*;

import javax.servlet.http.HttpSession;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import puri.feed.entity.User;
import puri.feed.model.UserModel;
import puri.feed.service.UserService;


@Log4j2
@RequiredArgsConstructor   
@Controller
@RequestMapping("/user")
public class UserController {
   
	private final UserService userService;

	@GetMapping("")
	public String getUser(Model model) {
		List<UserModel> userList = userService.getUser();
        System.out.println("[debug] userList : " + userList);
        model.addAttribute("userList", userList);
		return "content/userlist";
	}  

	@GetMapping("join1")
	public String join1(){
		
		return "content/user";
	}

	@PostMapping("join1")
    public String join1(User user, Model model) {
        long userId;
        String result ="";
        userId = userService.join(user);

        result = String.format("%d",userId);
        model.addAttribute("userId", result);
        System.out.println("[debug] userId : " + result);   
        return "redirect:user/join1";
        // return getUser(model);
    }

    @GetMapping("joinForm") // 회원가입
    public String joinForm() {
        return "user/joinForm";
    }

    //로그아웃 기능 구현 
    @GetMapping("logout")
    public String logout(HttpSession session){
        session.removeAttribute("user");
        return "redirect:/";
    }

    @GetMapping("loginForm") // 로그인
    public String loginForm() {
        return "user/loginForm";
    }

    @GetMapping("userForm") // 사용자 수정
    public String userForm(HttpSession session, Model model) {

        String returnMsg = "";

        User s_user = (User) session.getAttribute("user");

        User m_user = userService.UserDetailByUserId(s_user.getUserid()); 

        if (m_user == null) {
            returnMsg = "없는 사용자 입니다.";
        }
        else {
            returnMsg = "회원 정보 수정 합니다.^^";
            model.addAttribute("userDetail", m_user);
        }
        System.out.println("userDetail: " + m_user);
        System.out.println("returnMsg: " + returnMsg);
        model.addAttribute("returnMsg", returnMsg);

        return "user/userForm";
    }


}
