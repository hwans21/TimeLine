package TwitchToYoutube.timeline;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest
public class StreamTest {
    /*
        testdata 세팅
        - 하루 6시간 5일

        record 2시간
        not 1시간
        record 2시간

        clip 5분마다 하나씩

     */

//    @Autowired
//    StreamRepository streamRepository;
//    @Autowired
//    ClipRepository clipRepository;
//    @Autowired
//    RecordRepository recordRepository;
//
//    @Test
//    @Transactional
//    @Rollback(false)
//    public void testList() throws Exception {
//        List<StreamCheck> streamChecks = getStreamList();
//        List<Record> records = getRecordList(streamChecks);
//        List<Clip> clips = getClipList(streamChecks);
//
//
//        for(StreamCheck streamCheck : streamChecks){
//            Long id = streamRepository.save(streamCheck);
//            System.out.println(streamRepository.find(id).toString());
//        }
//        for(Clip clip : clips){
//            Long id = clipRepository.save(clip);
//            System.out.println(clipRepository.find(id).toString());
//        }
//        for(Record record : records){
//            Long id = recordRepository.save(record);
//            clipRepository.validClipUpdate(record);
//        }
//        int i = 0;
//        for(Record record : records){
//            List<Clip> validClips = clipRepository.getValidClips(record);
//            for(Clip clip : validClips){
//                i++;
//                System.out.printf("%d. %s\n", i, clip.toString());
//            }
//        }
//
//
//
//
//        //given
//
//        //when
//
//        //then
//    }
//    public List<StreamCheck> getStreamList(){
//        Date now = new Date();
//        Calendar cal = Calendar.getInstance();
//        List<StreamCheck> list = new ArrayList<>();
//
//        for(int i = 0; i<5;i++){
//            cal.setTime(now);
//            cal.add(Calendar.DATE, i);
//            list.add(new StreamCheck("subtanker", cal.getTime(), 60*60*6));
//        }
//        return list;
//    }
//
//    public List<Record> getRecordList(List<StreamCheck> streams){
//        List<Record> list = new ArrayList<>();
//        Calendar cal = Calendar.getInstance();
//        for(StreamCheck stream : streams){
//            cal.setTime(stream.getStreamStart());
//            list.add(new Record(cal.getTime(), 60*60*2,"", stream));
//            cal.add(Calendar.HOUR, 3);
//            list.add(new Record(cal.getTime(), 60*60*2,"", stream));
//        }
//        return list;
//    }
//
//    public List<Clip> getClipList(List<StreamCheck> streams){
//        List<Clip> list = new ArrayList<>();
//        Calendar cal = Calendar.getInstance();
//        for(StreamCheck stream : streams){
//            cal.setTime(stream.getStreamStart());
//            for(int i=0; i<(int) stream.getStreamSecond()/300; i++){
//                cal.add(Calendar.SECOND,300);
//                //  Clip(String clipWriter, String clipTitle, Date clipTime, Integer clipCount, StreamCheck stream)
//                list.add(new Clip("testuser"+i, "testtitle"+i, cal.getTime(),0,stream, null));
//            }
//        }
//        return list;
//    }



}
