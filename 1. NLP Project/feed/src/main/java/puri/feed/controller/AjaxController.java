package puri.feed.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import puri.feed.dto.SendDto;


@RestController
public class AjaxController {

    @GetMapping("ajax")
    public String ajax(){
        return "ajax/ajax";
    }

    @PostMapping("send")
    public String ajaxSend(SendDto dto){

        System.out.println(dto.getResult());
        // model.addAttribute("msg",dto.getResult());
        return dto.getResult();
    }

}
