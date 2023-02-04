package TwitchToYoutube.timeline.controller;

import TwitchToYoutube.timeline.entity.PageVO;
import TwitchToYoutube.timeline.entity.Youtube;
import TwitchToYoutube.timeline.manager.SessionManager;
import TwitchToYoutube.timeline.manager.YoutubeService;
import TwitchToYoutube.timeline.repository.YoutubeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.HashMap;
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


    @PostMapping("/insert")
    public String insertYoutube(HttpServletRequest request){
        String referer = request.getHeader("Referer");
        service.insertService(request);
        return "redirect:/manage/1";
    }
    @GetMapping("/{pageNum}")
    public String managePage(Model model,@PathVariable("pageNum") int pageNum){
        PageVO vo = service.getPageVO(pageNum);
        List<Youtube> list = service.getYoutubeList(vo);
        model.addAttribute("list",list);
        model.addAttribute("paging",vo);
        return "youtube_list";
    }

    @ResponseBody
    @GetMapping("/find")
    public Map<String, String> findYoutube(@RequestParam String youtubeId){
        Map<String, String> map  = service.findYoutube(youtubeId);
        return map;
    }

    @PostMapping("/update")
    public String updateYoutube(HttpServletRequest request){
        return service.updateYoutube(request);
    }

    @PostMapping("/remove")
    public String removeYoutube(HttpServletRequest request){
        return service.removeYoutube(request);
    }
}
