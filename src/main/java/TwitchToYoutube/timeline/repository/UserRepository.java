package TwitchToYoutube.timeline.repository;

import TwitchToYoutube.timeline.entity.User;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

@Repository
public class UserRepository {
    @PersistenceContext
    private EntityManager em;

    public User find(Long id){
        return em.find(User.class, id);
    }

    public void save(User vo){
        em.persist(vo);
    }

//
//    public void update(User vo){
//
//    }

    public void delete(User vo){
        em.remove(vo);
    }


}
