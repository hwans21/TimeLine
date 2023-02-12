package TwitchToYoutube.timeline.repository;

import TwitchToYoutube.timeline.entity.PageVO;
import TwitchToYoutube.timeline.entity.Timeline;
import TwitchToYoutube.timeline.entity.Youtube;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.util.*;

@Repository
public class YoutubeRepository {
    private final Logger log = LogManager.getLogger(this.getClass());

    @PersistenceContext
    private EntityManager em;

    @Transactional
    public Long save(Youtube vo){
        em.persist(vo);
        return vo.getYoutubeId();
    }
    public Youtube find(Long id){
        return em.find(Youtube.class, id);
    }

    public List<Youtube> getList(PageVO vo, String streamer){
        String sql = "SELECT y FROM Youtube y " +
                "WHERE y.userId.userLogin = :streamer " +
                "ORDER BY y.youtubeRecordStart DESC";
        List<Youtube> youtubelist
                = em.createQuery(sql, Youtube.class)
                .setParameter("streamer",streamer)
                .setFirstResult((vo.getCurrentPage()-1)*vo.getPageOfCount())
                .setMaxResults(vo.getPageOfCount())
                .getResultList();
        return youtubelist;
    }

    @Transactional
    public int update(Youtube vo){
        System.out.println(vo.toString());
        String sql = "UPDATE Youtube y " +
                "SET y.userId = :userId, " +
                "y.youtubeTitle = :youtubeTitle, " +
                "y.youtubeRecordStart = :youtubeRecordStart, " +
                "y.youtubeLength = :youtubeLength, " +
                "y.youtubeUrl = :youtubeUrl " +
                "WHERE y.youtubeId = :youtubeId";
        return em.createQuery(sql)
                .setParameter("userId", vo.getUserId())
                .setParameter("youtubeTitle",vo.getYoutubeTitle())
                .setParameter("youtubeRecordStart", vo.getYoutubeRecordStart())
                .setParameter("youtubeLength", vo.getYoutubeLength())
                .setParameter("youtubeUrl", vo.getYoutubeUrl())
                .setParameter("youtubeId", vo.getYoutubeId())
                .executeUpdate();
    }

    @Transactional
    public void remove(Youtube vo){
        String sql = "UPDATE FROM Timeline t " +
                "SET t.youtubeId.youtubeId = null " +
                "where t.youtubeId.youtubeId = :youtubeId";
        em.createQuery(sql)
                .setParameter("youtubeId", vo.getYoutubeId())
                .executeUpdate();
        em.remove(vo);
    }



}
