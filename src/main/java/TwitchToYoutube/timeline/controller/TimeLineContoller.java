package TwitchToYoutube.timeline.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class TimeLineContoller {

    @GetMapping("/")
    public String indexPage(){
        return "index";
    }
    @GetMapping("/dashboard")
    public String dashBoardPage(){
        return "dashboard";
    }
}
