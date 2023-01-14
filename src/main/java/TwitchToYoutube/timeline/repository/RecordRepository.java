package TwitchToYoutube.timeline.repository;

import TwitchToYoutube.timeline.entity.Record;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

@Repository
public class RecordRepository {
    @PersistenceContext
    private EntityManager em;

    public Long save(Record record){
        em.persist(record);
        return record.getRecordId();
    }
    public Record find(Long id){
        return em.find(Record.class, id);
    }
}
