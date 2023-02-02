package TwitchToYoutube.timeline.controller;

import TwitchToYoutube.timeline.entity.Timeline;
import TwitchToYoutube.timeline.manager.Common;
import TwitchToYoutube.timeline.manager.SessionManager;
import TwitchToYoutube.timeline.manager.TimelineService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;

@RequestMapping("/timeline")
@Controller
@RequiredArgsConstructor
public class TimeLineContoller {

    private Common common;
    private final SessionManager sessionManager;
    @Autowired
    private TimelineService service;
    @GetMapping("")
    public String timelinePage(){
        return "timeline_list";
    }

    @ResponseBody
    @PostMapping("/insert")
    public String insertTimeLine(HttpServletRequest request, @RequestBody Map<String, String> map){
        if(service.insertTimeline(request, map)) {
            return "정상";
        }else{
            return "실패";
        }
    }
    @ResponseBody
    @PostMapping("/copylist")
    public String selectCopyTimeLine(HttpServletRequest request, @RequestBody Map<String, String> map){
        String result = "";
        List<Timeline> list = service.selectCopyTimeLine(request, map);
        for(Timeline t : list){
            result += common.secondToStr(t.getTimelineSec())+" ";
            result += t.getTimelineTitle() +"\n";
        }

        return result;
    }


}
