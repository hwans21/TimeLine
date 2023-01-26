package TwitchToYoutube.timeline.repository;

import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

@Repository
public class TimelineRepository {
    @PersistenceContext
    private EntityManager em;
}
