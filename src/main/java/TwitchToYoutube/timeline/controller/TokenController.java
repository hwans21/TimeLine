package TwitchToYoutube.timeline.controller;

import TwitchToYoutube.timeline.entity.User;
import TwitchToYoutube.timeline.enums.AuthType;
import TwitchToYoutube.timeline.manager.SessionManager;
import TwitchToYoutube.timeline.repository.UserRepository;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

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
public class TokenController {
    private static final String clientId = "jcmj66qmli9btfjppfvoxnh1i46vjt";
    private final SessionManager sessionManager;

    @Autowired
    UserRepository repository;

    @Transactional
    @GetMapping("/login")
    public String login(HttpServletRequest request, HttpServletResponse response, Model model){
        String code = request.getParameter("code");
        String scope = request.getParameter("scope");
        String state = request.getParameter("state");
        String error = request.getParameter("error");
        String error_description = request.getParameter("error_description");
        if(code != null) {
            String token = getToken(code);
            Map<String, String> user = getUser(token);
            sessionManager.createSession(user, response);
//            map.put("display_name",(String) dataJobj.get("display_name"));
//            map.put("login",(String) dataJobj.get("login"));
//            map.put("id",(String) dataJobj.get("id"));

            User u = repository.find(Long.parseLong(user.get("id")));
            // 테이블에 유저가 없다면 insert
            if(u == null){
                User in = new User(Long.parseLong(user.get("id")),user.get("login"), user.get("display_name"), AuthType.WATCHER);
                repository.save(in);

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
            return "timeline_list";
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
        return "redirect:/timeline";
    }

    public Map<String,String> getUser(String token){
        HashMap<String, String> map = new HashMap<>();

        Map<String, String> headers = new HashMap<>();
        Map<String, String> datas = new HashMap<>();

        headers.put("Authorization", token);
        headers.put("Client-Id", clientId);
        String response = getRequest("https://api.twitch.tv/helix/users", headers, datas, "GET");
        JSONParser parser = new JSONParser();
        JSONObject jobj = null;
        try {
            jobj = (JSONObject) parser.parse(response);
            JSONArray data = (JSONArray) jobj.get("data");
            JSONObject dataJobj = (JSONObject)data.get(0);
            map.put("display_name",(String) dataJobj.get("display_name"));
            map.put("login",(String) dataJobj.get("login"));
            map.put("id",(String) dataJobj.get("id"));

        } catch (ParseException e) {
            e.printStackTrace();
        }

        return map;

    }
    public String getToken(String code){
        String token="";
        HashMap<String, String> headers = new HashMap<>();
        HashMap<String, String> datas = new HashMap<>();
        datas.put("client_id", clientId);
        datas.put("client_secret", getSecretkey());
        datas.put("code",code);
        datas.put("grant_type","authorization_code");
        datas.put("redirect_uri","http://192.168.0.9:8080/getNick");
        String response = getRequest("https://id.twitch.tv/oauth2/token", headers, datas, "post");
        JSONParser parser = new JSONParser();
        JSONObject jobj = null;
        try {
            jobj = (JSONObject) parser.parse(response);
            String accessToken = (String) jobj.get("access_token");
            String tokenType = (String) jobj.get("token_type");
            tokenType=tokenType.substring(0,1).toUpperCase()+tokenType.substring(1);
            token = tokenType+" "+accessToken;


        } catch (ParseException e) {
            e.printStackTrace();
        }


        return token;
    }

    public static String getSecretkey(){
        String secretkey="";
        BufferedReader reader = null;
        try {
            File file = new ClassPathResource("twitchsecretkey").getFile();
            reader = new BufferedReader(new FileReader(file));
            String str;
            while ((str = reader.readLine()) != null) {
                secretkey = str;
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                reader.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return secretkey;
    }

    public static String getRequest(String url, Map<String, String> headers, Map<String, String> datas, String method){
        String result="";

        if(method.equalsIgnoreCase("get")){
            String params = "?";
            for(String key : datas.keySet()){
                params += key+"="+datas.get(key)+"&";
            }
            if(params.length()!=1){
                params = params.substring(0, params.length()-1);

            }else{
                params="";
            }
            try {
                System.out.println(url+params);
                URL url1 = new URL(url+params);
                HttpURLConnection connection = (HttpURLConnection) url1.openConnection();
                connection.setRequestMethod("GET");
                for(String key : headers.keySet()){
                    connection.setRequestProperty(key, headers.get(key));
                }
                connection.setDoOutput(true);
                int responseCode = connection.getResponseCode();
                BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                String inputLine;
                StringBuffer response = new StringBuffer();

                while ((inputLine = in.readLine()) != null) {
                    response.append(inputLine);
                }
                in.close();
                // print result
                System.out.println("HTTP 응답 코드 : " + responseCode);
                System.out.println("HTTP body : " + response.toString());
                result = response.toString();
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();

            }
        }
        else if(method.equalsIgnoreCase("post")){
            String params = "?";
            for(String key : datas.keySet()){
                params += key+"="+datas.get(key)+"&";
            }

            params = params.substring(1, params.length()-1);
            System.out.println(url);
            System.out.println(params);
            try {
                URL url1 = new URL(url);
                HttpURLConnection connection = (HttpURLConnection) url1.openConnection();

                connection.setRequestMethod("POST");

                for(String key : headers.keySet()){
                    connection.setRequestProperty(key, headers.get(key));
                }

                connection.setDoOutput(true);
                DataOutputStream outputStream = new DataOutputStream(connection.getOutputStream());
                outputStream.writeBytes(params);
                outputStream.flush();
                outputStream.close();

                int responseCode = connection.getResponseCode();
                BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                String inputLine;
                StringBuffer response = new StringBuffer();

                while ((inputLine = in.readLine()) != null) {
                    response.append(inputLine);
                }
                in.close();
                // print result
                System.out.println("HTTP 응답 코드 : " + responseCode);
                System.out.println("HTTP body : " + response.toString());
                result = response.toString();
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();

            }
        }
        return result;
    }




}
