package TwitchToYoutube.timeline.controller;

import TwitchToYoutube.timeline.entity.PageVO;
import TwitchToYoutube.timeline.entity.User;
import TwitchToYoutube.timeline.entity.Youtube;
import TwitchToYoutube.timeline.manager.SessionManager;
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
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;



@Controller
@RequiredArgsConstructor
public class YoutubeController {
    private final SessionManager sessionManager;

    @Autowired
    private YoutubeRepository repository;

    @Transactional
    @PostMapping("/testinsert")
    public String insertYoutube(HttpServletRequest request){
        /*
            유저id, youtubetitle, youtubeRecordStarttime,
         */

        Map<String, String> map = (Map<String, String>) sessionManager.getSession(request);
        Long userid = Long.parseLong(map.get("id"));
        String url = request.getParameter("testurl");
        String recordStartTime = request.getParameter("testdate");
        recordStartTime = "230101_123040";//testdate
        Youtube vo = getYoutubeVo(userid,recordStartTime,url);
        repository.save(vo);

        return "redirect:/manage";
    }
    @GetMapping("/manage/{pageNum}")
    public String managePage(Model model,@PathVariable("pageNum") int pageNum){

        PageVO vo = new PageVO(pageNum, 10);
        PageVO vo2 = new PageVO(pageNum+1, 10);
        List<Youtube> list = repository.getList(vo);
        List<Youtube> list2 = repository.getList(vo2);
        if (pageNum > 1) {
            vo.setPrev(true);
        } else {
            vo.setPrev(false);
        }
        if(list2.size() > 0){
            vo.setNext(true);
        } else{
            vo.setNext(false);
        }
        model.addAttribute("list",list);
        model.addAttribute("paging",vo);
        return "youtube_list";
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
