package TwitchToYoutube.timeline.controller;

import TwitchToYoutube.timeline.entity.Timeline;
import TwitchToYoutube.timeline.service.CommonService;
import TwitchToYoutube.timeline.manager.SessionManager;
import TwitchToYoutube.timeline.service.TimelineService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Map;

@RequestMapping("/timeline")
@Controller
@RequiredArgsConstructor
public class TimeLineContoller {

    private CommonService common;
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
        if(list.size()==0) result=" ";
        return result;
    }
    @ResponseBody
    @PostMapping("/list")
    public List<Timeline> timelineList(HttpServletRequest request, @RequestBody Map<String, String> map){
        String streamer = sessionManager.findCookie(request,"streamer").getValue();
        List<Timeline> list = null;
        String start = map.get("start");
        String end = map.get("end");
        String order = map.get("order");

        int pageNum = Integer.parseInt(map.get("pageNum"));
        list = service.getList(start, end, order, pageNum,streamer);
        for(Timeline t : list){
            SimpleDateFormat format = new SimpleDateFormat("yyMMdd_HHmmss");
            t.setTimeFormat(format.format(t.getTimelineTime()));
        }

        return list;
    }

    @ResponseBody
    @PostMapping("/countup")
    public String countup(HttpServletRequest request, @RequestBody Map<String, String> map){
        try{
            String idstr = map.get("id");
            Long id = Long.parseLong(idstr);
            service.countup(id);
            return "성공";
        }catch (Exception e){
            e.printStackTrace();
            return "실패";
        }
        


    }
}
