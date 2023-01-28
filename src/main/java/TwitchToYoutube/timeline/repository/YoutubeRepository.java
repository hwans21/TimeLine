package TwitchToYoutube.timeline.repository;

import TwitchToYoutube.timeline.entity.User;
import TwitchToYoutube.timeline.entity.Youtube;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.io.IOException;
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

    public List<Youtube> getPage(int page){
        int pagesize = 10;
        String sql = "SELECT y FROM YOUTUBE y";
        List<Youtube> youtubelist
                = em.createQuery(sql, Youtube.class)
                .setFirstResult(page*pagesize)
                .setMaxResults(pagesize)
                .getResultList();
        return youtubelist;
    }



}
