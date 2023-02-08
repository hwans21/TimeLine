package TwitchToYoutube.timeline.controller;

import TwitchToYoutube.timeline.entity.PageVO;
import TwitchToYoutube.timeline.entity.Youtube;
import TwitchToYoutube.timeline.manager.SessionManager;
import TwitchToYoutube.timeline.service.CommonService;
import TwitchToYoutube.timeline.service.YoutubeService;
import TwitchToYoutube.timeline.repository.YoutubeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Map;



@RequestMapping("/manage")
@Controller
@RequiredArgsConstructor
public class YoutubeController {
    private final SessionManager sessionManager;

    @Autowired
    private YoutubeRepository repository;
    @Autowired
    private YoutubeService service;

    private CommonService common;

    @GetMapping("/{pageNum}")
    public String managePage(HttpServletRequest request,Model model, @PathVariable("pageNum") int pageNum){

        PageVO vo = service.getPageVO(pageNum);
        List<Youtube> list = service.getYoutubeList(vo);
        model.addAttribute("list",list);
        model.addAttribute("paging",vo);
        Cookie cookie = sessionManager.findCookie(request, "streamer");
        model.addAttribute("streamer",cookie.getValue());
        return "youtube_list";
    }

    @ResponseBody
    @GetMapping("/find")
    public Map<String, String> findYoutube(@RequestParam String youtubeId){

        Map<String, String> map  = service.findYoutube(youtubeId);
        return map;
    }
    @PostMapping("/insert")
    public String insertYoutube(HttpServletRequest request){
        Map<String, String> user = (Map<String, String>) sessionManager.getSession(request);
        Cookie streamer = common.findCookie(request, "streamer");
        if(streamer.getValue().equals(user.get("login"))){
            service.insertService(request);
        }
        String referer = request.getHeader("Referer");

        return "redirect:/manage/1";
    }

    @PostMapping("/update")
    public String updateYoutube(HttpServletRequest request){
        Map<String, String> user = (Map<String, String>) sessionManager.getSession(request);
        Cookie streamer = common.findCookie(request, "streamer");
        if(streamer.getValue().equals(user.get("login"))){
            return service.updateYoutube(request);
        }
        String referer = request.getHeader("Referer");

        return "redirect:"+referer;
    }

    @PostMapping("/remove")
    public String removeYoutube(HttpServletRequest request){
        Map<String, String> user = (Map<String, String>) sessionManager.getSession(request);
        Cookie streamer = common.findCookie(request, "streamer");
        if(streamer.getValue().equals(user.get("login"))){
            return service.removeYoutube(request);
        }
        String referer = request.getHeader("Referer");
        return "redirect:"+referer;
    }


}
