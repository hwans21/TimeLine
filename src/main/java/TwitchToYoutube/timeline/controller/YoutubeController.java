package TwitchToYoutube.timeline.controller;

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
import org.springframework.web.bind.annotation.PostMapping;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
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
        Long youtubeLength = new Long(Integer.parseInt(youtubeInfo.get("length")));


        Youtube vo = new Youtube(userid,youtubeTitle,date,youtubeLength,url);
        repository.save(vo);

        return "redirect:/";
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
}
