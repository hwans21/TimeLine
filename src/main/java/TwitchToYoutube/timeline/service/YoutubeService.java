package TwitchToYoutube.timeline.service;

import TwitchToYoutube.timeline.entity.PageVO;
import TwitchToYoutube.timeline.entity.User;
import TwitchToYoutube.timeline.entity.Youtube;
import TwitchToYoutube.timeline.manager.SessionManager;
import TwitchToYoutube.timeline.repository.TimelineRepository;
import TwitchToYoutube.timeline.repository.UserRepository;
import TwitchToYoutube.timeline.repository.YoutubeRepository;
import lombok.RequiredArgsConstructor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.*;

@Service
@RequiredArgsConstructor
public class YoutubeService {
    private final Logger log = LogManager.getLogger(this.getClass());

    private final SessionManager sessionManager;
    @Autowired
    private YoutubeRepository repository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private TimelineRepository timelineRepository;

    private CommonService common;
    public boolean insertService(HttpServletRequest request,Map<String, String> param){
        log.debug("Youtube Insert Service");
        try{
            Map<String, String> map = (Map<String, String>) sessionManager.getSession(request);
            Long userid = Long.parseLong(map.get("id"));
            String url = param.get("url");
            String recordStartTime = param.get("date");
            Youtube vo = getYoutubeVo(userid,recordStartTime,url);
            vo.setYoutubeStremaer(sessionManager.findCookie(request,"streamer").getValue());
            repository.save(vo);
            Calendar cal = Calendar.getInstance();
            cal.setTime(vo.getYoutubeRecordStart());
            cal.add(Calendar.SECOND, common.strToSecond(vo.getYoutubeLength()));
            String streamer = sessionManager.findCookie(request,"streamer").getValue();
            timelineRepository.updateTimeline(vo, vo.getYoutubeRecordStart(),cal.getTime(),streamer);
            return true;
        } catch (Exception e){
            common.getPrintStackTrace(e);
            return false;
        }

    }
    public PageVO getPageVO(HttpServletRequest request, int num){
        log.debug("Youtube getPaging Service");
        String streamer = sessionManager.findCookie(request,"streamer").getValue();
        PageVO vo = new PageVO(num, 10);
        PageVO vo2 = new PageVO(num+1, 10);
        List<Youtube> list = getYoutubeList(vo,streamer);
        List<Youtube> list2 = getYoutubeList(vo2,streamer);
        if (num > 1) {
            vo.setPrev(true);
        } else {
            vo.setPrev(false);
        }
        if(list2.size() > 0){
            vo.setNext(true);
        } else{
            vo.setNext(false);
        }
        return vo;
    }

    public Map<String, String> findYoutube(String youtubeId){
        log.debug("Youtube OneSelect Service");
        Youtube youtube = repository.find(Long.parseLong(youtubeId));
        SimpleDateFormat format = new SimpleDateFormat("yyMMdd_HHmmss");
        String startTime = format.format(youtube.getYoutubeRecordStart());
        String youtubeUrl = youtube.getYoutubeUrl();
        String modYoutubeId = Long.toString(youtube.getYoutubeId());
        Map<String, String> map = new HashMap<>();
        map.put("id", modYoutubeId);
        map.put("time",startTime);
        map.put("url", youtubeUrl);
        return map;
    }
    public List<Youtube> getYoutubeList(PageVO vo, String streamer){
        log.debug("Youtube Select Service");
        return repository.getList(vo,streamer);
    }
    public Map<String, String> getYoutubeInfo(String url){
        log.debug("Youtube OneSelect Service");
        Map<String, String> map = new HashMap<>();
        try {
            Connection conn = Jsoup.connect(url);
            Connection.Response response = conn.method(Connection.Method.GET).execute();
            Document document = response.parse();
            Elements scripts = document.getElementsByTag("script");
            for(Element script : scripts){
                String scripthtml = script.html();
                if(scripthtml.contains("var ytInitialPlayerResponse")){
                    String responseJson =
                            scripthtml.replace("var ytInitialPlayerResponse = ","")
                                    .split(";var")[0];
                    JSONParser parser = new JSONParser();
                    JSONObject ytInfoObj = (JSONObject) parser.parse(responseJson);
                    JSONObject videoDetails = (JSONObject) ytInfoObj.get("videoDetails");
                    String title = (String) videoDetails.get("title");
                    String length = (String) videoDetails.get("lengthSeconds");
                    map.put("title",title);
                    map.put("length",length);
                }

            }
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return map;
    }

    public String updateYoutube(HttpServletRequest request,Map<String, String> param){
        String referer = request.getHeader("Referer");
        String path = "";
        try {
            URL url = new URL(referer);
            path = url.getPath();
        } catch (MalformedURLException e) {
            throw new RuntimeException(e);
        }
        String streamer = sessionManager.findCookie(request, "streamer").getValue();
        Map<String, String> map = (Map<String, String>) sessionManager.getSession(request);
        Long userid = Long.parseLong(map.get("id"));
        Long youtubeId = Long.parseLong(param.get("id"));
        String url = param.get("url");
        String recordStartTime = param.get("date");
        Youtube vo = getYoutubeVo(userid,recordStartTime,url);
        vo.setYoutubeId(youtubeId);
        repository.update(vo);
        Calendar cal = Calendar.getInstance();
        cal.setTime(vo.getYoutubeRecordStart());
        cal.add(Calendar.SECOND, common.strToSecond(vo.getYoutubeLength()));
        timelineRepository.updateNotTimeline(vo, vo.getYoutubeRecordStart(),cal.getTime(), streamer);
        timelineRepository.updateTimeline(vo, vo.getYoutubeRecordStart(),cal.getTime(), streamer);
        return "redirect:"+path;
    }
    public boolean removeYoutube(Map<String, String> param){
         try{
             Long youtubeId = Long.parseLong(param.get("id"));
             log.debug("id="+youtubeId);
             Youtube vo = repository.find(youtubeId);
             repository.remove(vo);
             return true;
         } catch (Exception e){
             common.getPrintStackTrace(e);
             return false;
         }

    }
    public Youtube getYoutubeVo(Long userId, String recordStartTime, String url){
        String datefm = "yyMMdd_HHmmss";
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(datefm);
        Date date = null;
        try {
            date = simpleDateFormat.parse(recordStartTime);
        } catch (java.text.ParseException e) {
            e.printStackTrace();
        }
        User user = userRepository.find(userId);
        Map<String, String> youtubeInfo = getYoutubeInfo(url);
        String youtubeTitle = youtubeInfo.get("title");
        String youtubeLength = common.secondToStr(Integer.parseInt(youtubeInfo.get("length")));
        Youtube vo = new Youtube(user,youtubeTitle,date,youtubeLength,url);
        return vo;
    }



}
