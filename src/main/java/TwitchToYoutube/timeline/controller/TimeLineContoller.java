package TwitchToYoutube.timeline.controller;

import TwitchToYoutube.timeline.entity.Timeline;
import TwitchToYoutube.timeline.service.CommonService;
import TwitchToYoutube.timeline.manager.SessionManager;
import TwitchToYoutube.timeline.service.TimelineService;
import lombok.RequiredArgsConstructor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
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
    private final Logger log = LogManager.getLogger(this.getClass());

    private CommonService common;
    private final SessionManager sessionManager;
    @Autowired
    private TimelineService service;
    @GetMapping("")
    public String timelinePage(){
        log.debug("/timeline : 타임라인 페이지 진입");
        return "timeline_list";
    }
    
    @ResponseBody
    @PostMapping("/insert")
    public String insertTimeLine(HttpServletRequest request, @RequestBody Map<String, String> map){
        log.debug("/timeline/insert : 타임라인 INSERT 시작");
        return Long.toString(service.insertTimeline(request, map));
    }

    @ResponseBody
    @PostMapping("/oneselect")
    public Map<String, String> oneSelect(HttpServletRequest request, @RequestBody Map<String, String> map){
        log.debug("/timeline/oneselect : 타임라인 oneselect 시작");
        return service.oneSelect(request, map);
    }

    @ResponseBody
    @PostMapping("/copylist")
    public String selectCopyTimeLine(HttpServletRequest request, @RequestBody Map<String, String> map){
        log.debug("/timeline/copylist : 본인의 타임라인 LIST COPY 시작");
        String result = "";
        List<Timeline> list = service.selectCopyTimeLine(request, map);
        if(list.size()==0) result=" ";
        for(Timeline t : list){
            result += common.secondToStr(t.getTimelineSec())+" ";
            result += t.getTimelineTitle() +"\n";
        }
        return result;
    }
    @ResponseBody
    @PostMapping("/list")
    public List<Timeline> timelineList(HttpServletRequest request, @RequestBody Map<String, String> map){
        log.debug("/timeline/list : 타임라인 LIST 시작");
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
        log.debug("/timeline/countup : 타임라인 조회수 시작");
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
