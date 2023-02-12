package TwitchToYoutube.timeline.service;

import TwitchToYoutube.timeline.entity.Timeline;
import TwitchToYoutube.timeline.entity.User;
import TwitchToYoutube.timeline.manager.SessionManager;
import TwitchToYoutube.timeline.repository.TimelineRepository;
import TwitchToYoutube.timeline.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

@Service
@RequiredArgsConstructor
public class TimelineService {
    private final Logger log = LogManager.getLogger(this.getClass());


    private final SessionManager sessionManager;
    @Autowired
    private TimelineRepository repository;

    @Autowired
    private UserRepository userRepository;
    private CommonService common;
    public boolean insertTimeline(HttpServletRequest request, Map<String, String> map){
        log.debug("Timeline Insert Service");
        try{
            Map<String, String> user = (Map<String, String>) sessionManager.getSession(request);
            Long userid = Long.parseLong(user.get("id"));
            User userVO = userRepository.find(userid);
            String title =  common.scriptCheck(map.get("title"));
            String streamer = sessionManager.findCookie(request,"streamer").getValue();
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyMMdd_HHmmss");
            Date date = null;
            try {
                date = simpleDateFormat.parse(map.get("time"));
            } catch (java.text.ParseException e) {
                date = new Date();
            }
            Timeline vo = new Timeline(userVO, title, date, streamer, 0L);
            repository.save(vo);
            return true;
        } catch (Exception e){
            e.printStackTrace();
            return false;
        }
    }
    public List<Timeline> selectCopyTimeLine(HttpServletRequest request, Map<String, String> map){
        log.debug("Timeline selectCopy Service");
        List<Timeline> list = null;
        Map<String, Object> requestMap = new HashMap<>();
        try{
            Map<String, String> user = (Map<String, String>) sessionManager.getSession(request);
            Long userid = Long.parseLong(user.get("id"));
            String time = map.get("time");
            SimpleDateFormat format = new SimpleDateFormat("yyMMdd_HHmmss");
            Date start = format.parse(map.get("start"));
            Calendar cal = Calendar.getInstance();
            cal.setTime(start);
            cal.add(Calendar.SECOND, common.strToSecond(time));
            Date end = cal.getTime();
            requestMap.put("userid", userid);
            requestMap.put("start", start);
            requestMap.put("end", end);
            list = repository.copylist(requestMap);

        } catch (Exception e){
            e.printStackTrace();
        }
        System.out.println(list.size());
        return list;
    }

    public int countup(Long id){
        log.debug("Timeline countup Service");
        return repository.countup(id);
    }

    public List<Timeline> getList(String start, String end, String order, int pageNum, String steamer){
        log.debug("Timeline getList Service");
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd hhmmss");
        Date startDate = null;
        Date endDate = null;
        try {
            startDate = format.parse(start+" 000000");
            endDate = format.parse(end+" 235959");
        } catch (ParseException e) {
            e.printStackTrace();
        }
        if(startDate == null || endDate == null){
            endDate = new Date();
            Calendar cal = Calendar.getInstance();
            cal.setTime(endDate);
            cal.add(Calendar.DATE, -1);
            startDate = cal.getTime();
        }
        if(order == null || order.equals("")){
            order = "timeline_count";
        }
        List<Timeline> list = repository.getList(startDate, endDate, order, pageNum,steamer);
        return list;
    }

}
