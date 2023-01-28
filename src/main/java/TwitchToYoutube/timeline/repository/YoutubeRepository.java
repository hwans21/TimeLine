package TwitchToYoutube.timeline.repository;

import TwitchToYoutube.timeline.entity.PageVO;
import TwitchToYoutube.timeline.entity.Youtube;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.*;

@Repository
public class YoutubeRepository {
    @PersistenceContext
    private EntityManager em;

    public Long save(Youtube vo){
        em.persist(vo);
        return vo.getYoutubeId();
    }
    public Youtube find(Long id){

        return em.find(Youtube.class, id);
    }

    public List<Youtube> getList(PageVO vo){
        String sql = "SELECT y FROM Youtube y " +
                "ORDER BY y.youtubeRecordStart DESC";
        List<Youtube> youtubelist
                = em.createQuery(sql, Youtube.class)
                .setFirstResult((vo.getCurrentPage()-1)*vo.getPageOfCount())
                .setMaxResults(vo.getPageOfCount())
                .getResultList();
        return youtubelist;
    }


    public void remove(Youtube vo){
        em.remove(vo);
    }



}
