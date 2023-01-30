package TwitchToYoutube.timeline.manager;

import TwitchToYoutube.timeline.entity.PageVO;
import TwitchToYoutube.timeline.entity.Youtube;
import TwitchToYoutube.timeline.repository.YoutubeRepository;
import lombok.RequiredArgsConstructor;
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
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class YoutubeService {

    private final SessionManager sessionManager;
    @Autowired
    private YoutubeRepository repository;
    public boolean insertService(HttpServletRequest request){
        try{
            Map<String, String> map = (Map<String, String>) sessionManager.getSession(request);
            Long userid = Long.parseLong(map.get("id"));
            String url = request.getParameter("url");
            String recordStartTime = request.getParameter("date");
            Youtube vo = getYoutubeVo(userid,recordStartTime,url);
            repository.save(vo);
            return true;
        } catch (Exception e){
            return false;
        }

    }
    public PageVO getPageVO(int num){
        PageVO vo = new PageVO(num, 10);
        PageVO vo2 = new PageVO(num+1, 10);
        List<Youtube> list = getYoutubeList(vo);
        List<Youtube> list2 = getYoutubeList(vo2);
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
    public List<Youtube> getYoutubeList(PageVO vo){
       return repository.getList(vo);
    }
    public Map<String, String> getYoutubeInfo(String url){
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

    public String updateYoutube(HttpServletRequest request){
        String referer = request.getHeader("Referer");
        String path = "";
        try {
            URL url = new URL(referer);
            path = url.getPath();
        } catch (MalformedURLException e) {
            throw new RuntimeException(e);
        }
        Map<String, String> map = (Map<String, String>) sessionManager.getSession(request);
        Long userid = Long.parseLong(map.get("id"));
        Long youtubeId = Long.parseLong(request.getParameter("id"));
        String url = request.getParameter("url");
        String recordStartTime = request.getParameter("date");
        Youtube vo = getYoutubeVo(userid,recordStartTime,url);
        vo.setYoutubeId(youtubeId);
        repository.update(vo);
        return "redirect:"+path;
    }
    public String removeYoutube(HttpServletRequest request){
        String referer = request.getHeader("Referer");
        String path = "";
        try {
            URL url = new URL(referer);
            path = url.getPath();
        } catch (MalformedURLException e) {
            throw new RuntimeException(e);
        }
        Map<String, String> map = (Map<String, String>) sessionManager.getSession(request);
        Long userid = Long.parseLong(map.get("id"));
        Long youtubeId = Long.parseLong(request.getParameter("id"));

        Youtube vo = repository.find(youtubeId);
        repository.remove(vo);
        return "redirect:"+path;
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

        Map<String, String> youtubeInfo = getYoutubeInfo(url);
        String youtubeTitle = youtubeInfo.get("title");
        String youtubeLength = secondToStr(Integer.parseInt(youtubeInfo.get("length")));
        Youtube vo = new Youtube(userId,youtubeTitle,date,youtubeLength,url);

        return vo;
    }

    public String secondToStr(int second){
        String result="";
        result += (second/3600 < 10) ? "0"+second/3600 : ""+second/3600;
        result += ":";
        second = second % 3600;
        result += (second/60 < 10) ? "0"+second/60 : ""+second/60;
        result += ":";
        second = second % 60;
        result += (second < 10) ? "0"+second : ""+second;
        return result;
    }

    public int strToSecond(String str){
        int result = 0;
        String[] split = str.split(":");
        result += Integer.parseInt(split[0])*3600;
        result += Integer.parseInt(split[1])*60;
        result += Integer.parseInt(split[2]);
        return result;
    }

}
