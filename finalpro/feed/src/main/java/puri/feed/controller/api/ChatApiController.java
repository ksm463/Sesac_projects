package puri.feed.controller.api;

import javax.servlet.http.HttpSession;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import puri.feed.dto.ChatDto;
import puri.feed.entity.User;
import puri.feed.service.*;

@Log4j2
@RestController
@RequiredArgsConstructor
public class ChatApiController {

    private final ChatService chatService;

    // json 받음 = @RequestBody
    @PostMapping("api/chatbot/send")
    public ResponseEntity<String> send(@RequestBody ChatDto reqChat, HttpSession session) throws Exception {
        log.info("api chatbot send() 호출");

        User s_user = (User) session.getAttribute("user");

        // User m_user = userService.UserDetailByUserId(s_user.getUserid()); 

        ChatDto resChat = chatService.send(reqChat, s_user.getUserid());

        System.out.println("Chatbot res message: " + resChat.getCmsg());
        // model.addAttribute("msg",dto.getResult());

        return ResponseEntity.status(HttpStatus.OK).body(resChat.getCmsg());       
    }
    
}
