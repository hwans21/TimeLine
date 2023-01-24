package TwitchToYoutube.timeline.controller;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import javax.servlet.http.HttpServletRequest;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
public class TokenController {
    private static final String clientId = "jcmj66qmli9btfjppfvoxnh1i46vjt";
    @GetMapping("/login")
    public String login(HttpServletRequest request, Model model){
        String code = request.getParameter("code");
        String scope = request.getParameter("scope");
        String state = request.getParameter("state");
        String error = request.getParameter("error");
        String error_description = request.getParameter("error_description");
        if(code != null) {
            System.out.println("===========CORRECT===========");
            System.out.println("code : "+code);
            System.out.println("scope : "+scope);
            System.out.println("state : "+state);
            String token = getToken(code);
            System.out.println("token : "+token);
            Map<String, String> user = getUser(token);

            for(String key : user.keySet()){
                System.out.println(key+" : "+user.get(key));
            }

            model.addAttribute("display_name", user.get("display_name"));
            return "create_timeline";
        }else {
            System.out.println("===========ERROR===========");
            System.out.println("error : "+error);
            System.out.println("error_description : "+error_description);
            System.out.println("state : "+state);
            return "list_timeline";
        }

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
        datas.put("redirect_uri","http://localhost:8080/getNick");
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
