package TwitchToYoutube.timeline.controller;

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

    @GetMapping("/login")
    public String login(HttpServletRequest request){
        String code = request.getParameter("code");
        String scope = request.getParameter("scope");
        String state = request.getParameter("state");
        String error = request.getParameter("error");
        String error_description = request.getParameter("error_description");
        if(code != null) {
            System.out.println("===========CORRECT===========");
            System.out.println("code : "+code);
            System.out.println("scope : "+scope);
            System.out.println("state : "+state);
        }else {
            System.out.println("===========ERROR===========");
            System.out.println("error : "+error);
            System.out.println("error_description : "+error_description);
            System.out.println("state : "+state);
        }


        return "create_timeline";
    }


}
