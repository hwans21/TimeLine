package TwitchToYoutube.timeline.repository;

import TwitchToYoutube.timeline.entity.Clip;
import TwitchToYoutube.timeline.entity.Record;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.Calendar;
import java.util.List;

@Repository
public class ClipRepository {
    @PersistenceContext
    private EntityManager em;

    @Autowired
    RecordRepository recordRepository;

    public Long save(Clip clip){
        em.persist(clip);
        return clip.getClipId();
    }
    public Clip find(Long id){
        return em.find(Clip.class, id);
    }
    /*
        클립목록에 streamid = record.streamid
        시작시간 ~ 끝시간
        >> 해당 클립 목록 추출
        >> set record로 수정

     */
    public void validClipUpdate(Record record){
        Calendar cal = Calendar.getInstance();
        cal.setTime(record.getRecordStart());
        cal.add(Calendar.SECOND,record.getRecordSecond());
        String jpql = "UPDATE Clip c SET c.record = :record " +
                "where c.stream = :stream " +
                "and c.clipTime between :start and :end";
        em.createQuery(jpql)
                .setParameter("record",record)
                .setParameter("stream", record.getStream())
                .setParameter("start",record.getRecordStart())
                .setParameter("end", cal.getTime())
                .executeUpdate();
    }

    public List<Clip> getValidClips(Record record){
        String jpql = "select c from Clip c where c.record = :record";
        List<Clip> list = em.createQuery(jpql, Clip.class)
                .setParameter("record",record)
                .getResultList();
        return list;
    }
}
