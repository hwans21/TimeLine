package TwitchToYoutube.timeline.controller;

import TwitchToYoutube.timeline.manager.SessionManager;
import lombok.RequiredArgsConstructor;
import org.aspectj.apache.bcel.classfile.Code;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.tags.Param;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

@Controller
@RequiredArgsConstructor
public class TimeLineContoller {
    private final SessionManager sessionManager;

    @GetMapping("/")
    public String indexPage(HttpServletRequest request, Model model){
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
