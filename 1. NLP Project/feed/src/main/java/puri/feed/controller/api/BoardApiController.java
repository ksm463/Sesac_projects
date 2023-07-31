package puri.feed.controller.api;

import org.springframework.web.bind.annotation.*;


import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import puri.feed.dto.ResponseDto;
import puri.feed.entity.Board;
import puri.feed.entity.User;
import puri.feed.service.BoardService;

import javax.servlet.http.HttpSession;

import org.springframework.http.HttpStatus;

@Log4j2
@RequiredArgsConstructor
@RestController
public class BoardApiController {
    private final BoardService boardService;

    @PostMapping("api/board")
    public ResponseDto<Integer> saveBoard(@RequestBody Board board, HttpSession session) {
        log.info("save board session : " + session);
        User user = (User) session.getAttribute("user");

        Integer result= -1;
        if ( user == null)
        {
            result = -1;
        }
        else{
            result = 1;
            boardService.saveBoard(board, user);
        }

        return new ResponseDto<Integer>(HttpStatus.OK.value(), result);
    }

    @PutMapping("api/board/{id}")
    public ResponseDto<Integer> updateForm(@PathVariable Long id, @RequestBody Board board, HttpSession session) {
        log.info("update board session : " + session);

        User user = (User) session.getAttribute("user");

        Integer result= -1;
        if ( user == null)
        {
            result = -1;
        }
        else{
            result = 1;
            boardService.updateForm(id, board);
        }

        return new ResponseDto<Integer>(HttpStatus.OK.value(), result);
    }
    
    @DeleteMapping("api/board/{id}")
    public ResponseDto<Integer> deleteBoard(@PathVariable Long id, HttpSession session) {
        log.info("delete board session : " + session);
        User user = (User) session.getAttribute("user");

        Integer result= -1;
        if ( user == null)
        {
            result = -1;
        }
        else{
            result = 1;
            boardService.deleteBoard(id, user);
        }

        return new ResponseDto<Integer>(HttpStatus.OK.value(), result);
    }
}
