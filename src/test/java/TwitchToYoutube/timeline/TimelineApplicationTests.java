package TwitchToYoutube.timeline;

import TwitchToYoutube.timeline.entity.User;
import TwitchToYoutube.timeline.entity.Youtube;
import TwitchToYoutube.timeline.repository.TimelineRepository;
import TwitchToYoutube.timeline.repository.UserRepository;
import TwitchToYoutube.timeline.repository.YoutubeRepository;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;

@SpringBootTest
class TimelineApplicationTests {

	// Test할 테이블 연결
	@Autowired
	private TimelineRepository timelineRepository;
	@Autowired
	private UserRepository userRepository;
	@Autowired
	private YoutubeRepository  youtubeRepository;

	@Test
    @Transactional
    @Rollback(false)
	public void youtubeTest() throws Exception {
		Long testid = 10000L;
		String datestr = "220101_123040";
		List<String> urllist = new ArrayList<>();
		urllist.add("https://youtu.be/hT0lauVUmzI");
		urllist.add("https://youtu.be/aI6TCzFN080");
		urllist.add("https://youtu.be/XM3e7TUu70I");
		urllist.add("https://youtu.be/X3Ee58E8K6Y");
		urllist.add("https://youtu.be/5Jol-OQ8Uq4");
		urllist.add("https://youtu.be/ABXUurv9RUY");
		urllist.add("https://youtu.be/fQvO-hDxBVs");
		urllist.add("https://youtu.be/7KF1d4nlTmY");
		urllist.add("https://youtu.be/W_ds-OtqBbk");
		urllist.add("https://youtu.be/A8QE48asnKk");
		urllist.add("https://youtu.be/YRygr74jcZk");
		urllist.add("https://youtu.be/W-V724yaWwI");
		urllist.add("https://youtu.be/J7aQHGDsaf4");
		urllist.add("https://youtu.be/8Svr9cZrz9Q");
		urllist.add("https://youtu.be/xuU3zG0wpNY");
		urllist.add("https://youtu.be/iPIUXb1F6n4");
		urllist.add("https://youtu.be/wzPYT7uvXX0");
		urllist.add("https://youtu.be/OPVu_DnwVWE");
		urllist.add("https://youtu.be/detDJcFmXw0");
		urllist.add("https://youtu.be/X8_6QFKC3us");
		urllist.add("https://youtu.be/RoKxpy9qVzQ");
		urllist.add("https://youtu.be/dUZ_QPLUTgY");
		urllist.add("https://youtu.be/fDwVHkFVgCw");
		urllist.add("https://youtu.be/J0iquNkVCoo");
		urllist.add("https://youtu.be/TVfYwG25SMU");
		urllist.add("https://youtu.be/gYweFdW_YIw");
		urllist.add("https://youtu.be/Kz1gZ65hR-A");
		urllist.add("https://youtu.be/qf-m8FgsMjA");
		urllist.add("https://youtu.be/fi3OWCKphDQ");
		urllist.add("https://youtu.be/Q7xjv1jQUYg");
		urllist.add("https://youtu.be/0H4wGsQexpc");


		for(String url : urllist){
			String datefm = "yyMMdd_HHmmss";
			SimpleDateFormat simpleDateFormat = new SimpleDateFormat(datefm);
			Date date = null;
			try {
				date = simpleDateFormat.parse(datestr);
			} catch (java.text.ParseException e) {
				e.printStackTrace();
			}
			Calendar cal = Calendar.getInstance();
			cal.setTime(date);
			cal.add(Calendar.DATE, 1);
			datestr = simpleDateFormat.format(cal.getTime());
			Youtube vo = getYoutubeVo2(testid,datestr,url);
			youtubeRepository.save(vo);
		}

	}

	public static Map<String, String> getYoutubeInfo2(String url){
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

	public static Youtube getYoutubeVo2(Long userId, String recordStartTime, String url){
		String datefm = "yyMMdd_HHmmss";
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat(datefm);
		Date date = null;
		try {
			date = simpleDateFormat.parse(recordStartTime);
		} catch (java.text.ParseException e) {
			e.printStackTrace();
		}

		Map<String, String> youtubeInfo = getYoutubeInfo2(url);
		String youtubeTitle = youtubeInfo.get("title");
		String youtubeLength = secondToStr2(Integer.parseInt(youtubeInfo.get("length")));
		Youtube vo = new Youtube(userId,youtubeTitle,date,youtubeLength,url);

		return vo;
	}

	public static String secondToStr2(int second){
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

	public static int strToSecond2(String str){
		int result = 0;
		String[] split = str.split(":");
		result += Integer.parseInt(split[0])*3600;
		result += Integer.parseInt(split[1])*60;
		result += Integer.parseInt(split[2]);
		return result;
	}




}
