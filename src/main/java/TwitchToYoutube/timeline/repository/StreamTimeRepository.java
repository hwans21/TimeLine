package TwitchToYoutube.timeline.repository;

import TwitchToYoutube.timeline.entity.StreamTime;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

@Repository
public class StreamTimeRepository {
    @PersistenceContext
    private EntityManager em;

    public Long save(StreamTime streamTime){
        em.persist(streamTime);
        return streamTime.getStreamTimeId();
    }

    public StreamTime find(Long id){
        return em.find(StreamTime.class, id);
    }
}
