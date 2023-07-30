package puri.feed.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;


@Controller
public class HomeController {
    // @GetMapping({"", "/"})
    // public String index() {
    //     return "index";
    // }

    @GetMapping("/")
    public String home(Model model){
        String returnMsg = "HOME";
        model.addAttribute("returnMsg",returnMsg);
        return "index";
    }

}
