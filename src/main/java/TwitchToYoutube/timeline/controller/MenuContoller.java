package TwitchToYoutube.timeline.controller;

import TwitchToYoutube.timeline.manager.SessionManager;
import lombok.RequiredArgsConstructor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import javax.servlet.http.HttpServletRequest;

@Controller
@RequiredArgsConstructor
public class MenuContoller {
    private final SessionManager sessionManager;
    private final Logger log = LogManager.getLogger(this.getClass());
    @GetMapping("/")
    public String indexPage(HttpServletRequest request, Model model){
        log.debug("/ : 메인페이지 진입");
        return "login";
    }

//    ----------------------------------







}
