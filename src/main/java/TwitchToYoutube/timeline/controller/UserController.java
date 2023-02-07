package TwitchToYoutube.timeline.controller;

import TwitchToYoutube.timeline.entity.User;
import TwitchToYoutube.timeline.enums.AuthType;
import TwitchToYoutube.timeline.manager.SessionManager;
import TwitchToYoutube.timeline.service.CommonService;
import TwitchToYoutube.timeline.service.UserService;
import lombok.RequiredArgsConstructor;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

@Controller
@RequiredArgsConstructor
public class UserController {

    private final SessionManager sessionManager;

    @Autowired
    private UserService service;

    private CommonService common;
    private static final String clientId = "jcmj66qmli9btfjppfvoxnh1i46vjt";
    @PostMapping("/loginStart")
    public String loginStart(HttpServletRequest request,HttpServletResponse response){
        String streamer = request.getParameter("streamer");
        Cookie streamerCookies = new Cookie("streamer", streamer);
        response.addCookie(streamerCookies);
        String url = "https://id.twitch.tv/oauth2/authorize" +
                "?response_type=code" +
                "&client_id=" +clientId+
                "&redirect_uri=http://192.168.0.9:8080/login" +
                "&scope=user%3Aread%3Aemail" +
                "&state=c3ab8aa609ea11e793ae92361f002671";
        return "redirect:"+url;

    }


    @GetMapping("/login")
    public String login(HttpServletRequest request, HttpServletResponse response, Model model){
        String code = request.getParameter("code");
        String scope = request.getParameter("scope");
        String state = request.getParameter("state");
        String error = request.getParameter("error");
        String error_description = request.getParameter("error_description");
        if(code != null) {
            String token = common.getToken(code);
            Map<String, String> user = common.getUser(token);
            sessionManager.createSession(user, response);
//            map.put("display_name",(String) dataJobj.get("display_name"));
//            map.put("login",(String) dataJobj.get("login"));
//            map.put("id",(String) dataJobj.get("id"));

            User u = service.find(Long.parseLong(user.get("id")));
            // 테이블에 유저가 없다면 insert
            if(u == null){
                User in = new User(Long.parseLong(user.get("id")),user.get("login"), user.get("display_name"), AuthType.WATCHER);
                service.save(in);

            // login_id 및 display_name이 변경되었을 경우 update
            }else if(!user.get("login").equals(u.getUserLogin()) || !user.get("display_name").equals(u.getUserDisplay())){
               u.setUserLogin(user.get("login"));
               u.setUserDisplay(user.get("display_name"));
            }
            model.addAttribute("user",user);
            return "redirect:/timeline";
        }else {
            System.out.println("===========ERROR===========");
            System.out.println("error : "+error);
            System.out.println("error_description : "+error_description);
            System.out.println("state : "+state);
            return "redirect:/";
        }

    }


    @GetMapping("/logout")
    public String logout(HttpServletRequest request,HttpServletResponse response, Model model){
        Cookie[] cookies = request.getCookies();
        if(cookies != null) {
            for (int i = 0; i < cookies.length; i++) {
                // 쿠키의 유효시간을 0으로 설정하여 바로 만료시킨다.
                cookies[i].setMaxAge(0);
                // 응답에 쿠키 추가
                response.addCookie(cookies[i]);
            }
        }
        model.addAttribute("user",null);
        return "redirect:/";
    }




}
