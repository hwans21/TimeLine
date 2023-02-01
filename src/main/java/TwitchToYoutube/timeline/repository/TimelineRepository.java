package TwitchToYoutube.timeline.repository;

import TwitchToYoutube.timeline.entity.Timeline;
import TwitchToYoutube.timeline.entity.Youtube;
import TwitchToYoutube.timeline.manager.SessionManager;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

@Repository
public class TimelineRepository {
    @PersistenceContext
    private EntityManager em;

    @Transactional
    public Long save(Timeline vo){
        em.persist(vo);
        return vo.getTimelineId();
    }

    public List<Timeline> copylist(Map<String, Object> map){
        Long userid = (Long) map.get("id");
        Date start = (Date) map.get("start");
        Date end = (Date) map.get("end");

        String sql = "SELECT * FROM (SELECT t FROM Timeline t " +
                "WHERE t.userId = :userId ) as F " +
                "B" +

        return em.createQuery(sql)
                .setParameter();
    }
}
