package puri.feed.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

import javax.servlet.http.HttpSession;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;

import lombok.extern.log4j.Log4j2;
import puri.feed.entity.Board;
import puri.feed.entity.User;
import puri.feed.repository.BoardRepository;
import puri.feed.service.BoardService;
import puri.feed.service.UserService;
import lombok.RequiredArgsConstructor;

@Log4j2
@RequiredArgsConstructor
@Controller
public class DiaryController {

    private final BoardService boardService;
    private final UserService userService;
    private final BoardRepository boardRepository;

    @GetMapping("/diary/diaryForm")
    public String diaryForm(Model model, @PageableDefault(size = 100, sort = "id", direction = Sort.Direction.DESC) Pageable pageable, HttpSession session){
        User s_user = (User) session.getAttribute("user");
        User m_user = userService.UserDetailByUserId(s_user.getUserid()); 
        
        if (m_user != null) {
            log.info("m_user id: " + m_user.getId() + ", uid: "+m_user.getUserid());
            model.addAttribute("boardList", boardService.getBoardListByUser(m_user.getId()));
        }
        else
        {
            log.info("m_user =" + m_user);
        }

        return "board/diaryForm";

    }

    @ResponseBody
    @GetMapping("diary/boards")
    public Page<Board> pageUserDiaryList(@PageableDefault(size=100, sort="id", direction= Sort.Direction.DESC) Pageable pageable ) {
        Page<Board> pagingBoard=boardRepository.findAll(pageable);
        if (pagingBoard.isLast()) {
            // 분기처리 가능
        }
        List<Board> boards=pagingBoard.getContent();
        return pagingBoard;
    }

}