package TwitchToYoutube.timeline;

import TwitchToYoutube.timeline.entity.User;
import TwitchToYoutube.timeline.entity.Youtube;
import TwitchToYoutube.timeline.repository.TimelineRepository;
import TwitchToYoutube.timeline.repository.UserRepository;
import TwitchToYoutube.timeline.repository.YoutubeRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

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
	    //given
		User user = userRepository.find(181266387L);
		String inputDate = "230104_231023";
		String datefm = "yyMMdd_HHmmss";
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat(datefm);
		Date date = simpleDateFormat.parse(inputDate);

	    //when

	    //then
	}



}
