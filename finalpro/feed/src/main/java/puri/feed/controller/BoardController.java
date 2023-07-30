package puri.feed.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;

import lombok.extern.log4j.Log4j2;

import puri.feed.service.BoardService;
import lombok.RequiredArgsConstructor;

@Log4j2
@RequiredArgsConstructor
@Controller
public class BoardController {

    private final BoardService boardService;


    @GetMapping("/board/feed")
    public String index(Model model, @PageableDefault(size = 10, sort = "id", direction = Sort.Direction.DESC) Pageable pageable) {
        model.addAttribute("boardList", boardService.boardList(pageable));
        return "board/feed";
    }

    @GetMapping("/board/{id}")
    public String BoardDetail(@PathVariable Long id, Model model) {
        model.addAttribute("boardDetail", boardService.BoardDetail(id));
        return "board/detail";

    }

    @GetMapping("/board/saveForm")
    public String saveForm() {
        return "board/saveForm";
    }

    @GetMapping("/board/{id}/updateForm")
    public String updateForm(@PathVariable Long id, Model model) {
        model.addAttribute("updateBoard", boardService.BoardDetail(id));
        return "board/updateForm";
    }
}