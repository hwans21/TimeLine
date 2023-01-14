package TwitchToYoutube.timeline.repository;

import TwitchToYoutube.timeline.entity.StreamCheck;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

@Repository
public class StreamRepository {
    @PersistenceContext
    private EntityManager em;

    public Long save(StreamCheck stream){
        em.persist(stream);
        return stream.getStreamId();
    }

    public StreamCheck find(Long id){
        return em.find(StreamCheck.class, id);
    }
}
