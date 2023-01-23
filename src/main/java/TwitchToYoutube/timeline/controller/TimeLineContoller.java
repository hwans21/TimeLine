package TwitchToYoutube.timeline.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class TimeLineContoller {

    @GetMapping("/")
    public String indexPage(){
        return "list_timeline";
    }

//    ----------------------------------

    @GetMapping("/list")
    public String listPage(){
        return "list_timeline";
    }
    @GetMapping("/create")
    public String createPage(){
        return "create_timeline";
    }
    @GetMapping("/manage")
    public String managePage(){
        return "youtube_list";
    }

}
