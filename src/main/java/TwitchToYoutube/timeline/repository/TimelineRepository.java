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
        Long userid = (Long) map.get("userid");
        Date start = (Date) map.get("start");
        Date end = (Date) map.get("end");
        String sql = "SELECT t FROM Timeline t " +
                "WHERE t.userId = :userId AND " +
                "t.timelineTime BETWEEN :start AND :end ";
        List<Timeline> list = em.createQuery(sql, Timeline.class)
                .setParameter("userId", userid)
                .setParameter("start",start)
                .setParameter("end",end)
                .getResultList();
        for(Timeline t : list){
            int diffSec = (int) ((t.getTimelineTime().getTime() - start.getTime()) / 1000);
            t.setTimelineSec(diffSec);
        }
        return list;
    }

}
