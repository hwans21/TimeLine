package TwitchToYoutube.timeline.repository;

import TwitchToYoutube.timeline.entity.PageVO;
import TwitchToYoutube.timeline.entity.Timeline;
import TwitchToYoutube.timeline.entity.Youtube;
import TwitchToYoutube.timeline.service.CommonService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.Date;
import java.util.List;
import java.util.Map;

@Repository
public class TimelineRepository {
    private final Logger log = LogManager.getLogger(this.getClass());

    @PersistenceContext
    private EntityManager em;

    private CommonService common;
    @Transactional
    public Long save(Timeline vo){
        try{
            em.persist(vo);
        }catch (Exception e){
            common.getPrintStackTrace(e);
        }
        return vo.getTimelineId();
    }

    public Timeline find(Long id){
        return em.find(Timeline.class, id);
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

    public List<Timeline> getList(Date start, Date end, String order, int pageNum,String streamer){
        List<Timeline> list = null;
        try{

            String sql = "";
            if(order.equals("time")){
                sql = "SELECT t FROM Timeline t " +
                        "WHERE t.timelineTime BETWEEN :start AND :end " +
                        "AND t.youtubeId.youtubeId IS NOT NULL " +
                        "AND t.youtubeId.userId.userLogin = :streamer " +
                        "ORDER BY t.timelineTime DESC";
            } else if(order.equals("count")){
                sql = "SELECT t FROM Timeline t " +
                        "WHERE t.timelineTime BETWEEN :start AND :end " +
                        "AND t.youtubeId.youtubeId IS NOT NULL " +
                        "AND t.youtubeId.userId.userLogin = :streamer " +
                        "ORDER BY t.timelineCount DESC";
            }
            PageVO vo = new PageVO(pageNum, 10);

            list = em.createQuery(sql, Timeline.class)
                    .setParameter("start", start)
                    .setParameter("end",end)
                    .setParameter("streamer", streamer)
                    .setFirstResult((vo.getCurrentPage()-1)*vo.getPageOfCount())
                    .setMaxResults(vo.getPageOfCount())
                    .getResultList();
            for(Timeline t : list){
                int diffSec = (int) ((t.getTimelineTime().getTime() - t.getYoutubeId().getYoutubeRecordStart().getTime()) / 1000);
                t.setTimelineSec(diffSec);
            }
        }catch (Exception e){
            e.printStackTrace();
        }

        return list;
    }

    @Transactional
    public int countup(Long id){
        String sql = "UPDATE FROM Timeline t " +
                "SET t.timelineCount = t.timelineCount + 1 " +
                "WHERE t.timelineId = :timelineId";
        return em.createQuery(sql)
                .setParameter("timelineId",id)
                .executeUpdate();
    }

    @Transactional
    public int updateTimeline(Youtube youtubeId, Date start, Date end, String streamer){
        String sql = "UPDATE FROM Timeline t " +
                "SET t.youtubeId.youtubeId = :youtubeId " +
                "WHERE t.timelineTime BETWEEN :start AND :end " +
                "AND t.timelineStreamer = :streamer";

        return em.createQuery(sql)
                .setParameter("youtubeId",youtubeId.getYoutubeId())
                .setParameter("start",start)
                .setParameter("end",end)
                .setParameter("streamer", streamer)
                .executeUpdate();
    }
    @Transactional
    public int updateNotTimeline(Youtube youtubeId, Date start, Date end, String streamer){

        String sql = "UPDATE FROM Timeline t " +
                "SET t.youtubeId.youtubeId = null " +
                "WHERE t.youtubeId.youtubeId = :youtubeId " +
                "AND ( t.timelineTime < :start " +
                "OR t.timelineTime > :end ) " +
                "AND t.timelineStreamer = :streamer ";
        return em.createQuery(sql)
                .setParameter("youtubeId",youtubeId.getYoutubeId())
                .setParameter("start",start)
                .setParameter("end",end)
                .setParameter("streamer", streamer)
                .executeUpdate();
    }

}
