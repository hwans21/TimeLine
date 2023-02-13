package TwitchToYoutube.timeline.controller;

import TwitchToYoutube.timeline.entity.PageVO;
import TwitchToYoutube.timeline.entity.Youtube;
import TwitchToYoutube.timeline.manager.SessionManager;
import TwitchToYoutube.timeline.service.CommonService;
import TwitchToYoutube.timeline.service.YoutubeService;
import TwitchToYoutube.timeline.repository.YoutubeRepository;
import lombok.RequiredArgsConstructor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hibernate.Session;
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
    private final Logger log = LogManager.getLogger(this.getClass());

    @Autowired
    private YoutubeRepository repository;
    @Autowired
    private YoutubeService service;

    private CommonService common;


    @GetMapping("/{pageNum}")
    public String managePage(HttpServletRequest request,Model model, @PathVariable("pageNum") int pageNum){
        log.debug("/manage/{pageNum} : /manage/1 -> 유튜브 리스트 검색");
        String streamer = sessionManager.findCookie(request,"streamer").getValue();
        PageVO vo = service.getPageVO(request,pageNum);
        List<Youtube> list = service.getYoutubeList(vo, streamer);
        model.addAttribute("list",list);
        model.addAttribute("paging",vo);
        Cookie cookie = sessionManager.findCookie(request, "streamer");
        model.addAttribute("streamer",cookie.getValue());
        return "youtube_list";
    }

    @ResponseBody
    @GetMapping("/find")
    public Map<String, String> findYoutube(@RequestParam String youtubeId){
        log.debug("/manage/find : 유튜브 단일항목 검색");
        Map<String, String> map  = service.findYoutube(youtubeId);
        return map;
    }
    @ResponseBody
    @PostMapping("/insert")
    public String insertYoutube(HttpServletRequest request,@RequestBody Map<String, String> map){
        log.debug("/manage/insert : 유튜브 INSERT 시작");
        try{
            Map<String, String> user = (Map<String, String>) sessionManager.getSession(request);
            Cookie streamer = common.findCookie(request, "streamer");
            if(streamer.getValue().equals(user.get("login"))){
                if(service.insertService(request, map)){
                    return "성공";
                }else{
                    return "실패";
                }

            }else{
                return "권한부족";
            }
        }catch (Exception e){
            common.getPrintStackTrace(e);
            return "실패";
        }
    }

    @ResponseBody
    @PostMapping("/update")
    public String updateYoutube(HttpServletRequest request,@RequestBody Map<String, String> map){
        log.debug("/manage/update : 유튜브 update 시작");
        try{
            Map<String, String> user = (Map<String, String>) sessionManager.getSession(request);
            Cookie streamer = common.findCookie(request, "streamer");
            if(streamer.getValue().equals(user.get("login"))){
                service.updateYoutube(request, map);
                return "성공";
            }else{
                return "권한부족";
            }
        }catch (Exception e){
            common.getPrintStackTrace(e);
            return "실패";
        }
        
        

    }

    @ResponseBody
    @PostMapping("/remove")
    public String removeYoutube(HttpServletRequest request,@RequestBody Map<String, String> map){
        log.debug("/manage/remove : 유튜브 remove 시작");
        try {
            Map<String, String> user = (Map<String, String>) sessionManager.getSession(request);
            Cookie streamer = common.findCookie(request, "streamer");
            if(streamer.getValue().equals(user.get("login"))){
                if(service.removeYoutube(map)){
                    return "성공";
                } else{
                    return "실패";
                }

            }else{
                return "권한부족";
            }
        }catch (Exception e){
            common.getPrintStackTrace(e);
            return "실패";
        }

    }


}
