package TwitchToYoutube.timeline;

import TwitchToYoutube.timeline.entity.StreamTime;
import TwitchToYoutube.timeline.repository.StreamTimeRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;

@RunWith(SpringRunner.class)
@SpringBootTest
public class StreamTimeTest {
    @Autowired
    StreamTimeRepository streamTimeRepository;
    @Test
    @Transactional
    @Rollback(false)
    public void entitytest() throws Exception {
        //given
        Date date = new Date();
        StreamTime nick = new StreamTime("nick", date, date, 3600);
        Long i = streamTimeRepository.save(nick);
        //when
        StreamTime findTime = streamTimeRepository.find(i);

        System.out.println(findTime.toString());
        //then
    }
}
