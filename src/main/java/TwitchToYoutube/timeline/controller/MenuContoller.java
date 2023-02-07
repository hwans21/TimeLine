package TwitchToYoutube.timeline.controller;

import TwitchToYoutube.timeline.manager.SessionManager;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import javax.servlet.http.HttpServletRequest;

@Controller
@RequiredArgsConstructor
public class MenuContoller {
    private final SessionManager sessionManager;

    @GetMapping("/")
    public String indexPage(HttpServletRequest request, Model model){
        return "login";
    }

//    ----------------------------------







}
