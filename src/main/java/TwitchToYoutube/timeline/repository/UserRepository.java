package TwitchToYoutube.timeline.repository;

import TwitchToYoutube.timeline.entity.User;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;


@Repository
public class UserRepository {
    @PersistenceContext
    private EntityManager em;

    public User find(Long id){
        return em.find(User.class, id);
    }

    @Transactional
    public Long save(User vo){
        em.persist(vo);
        return vo.getUserId();
    }

//
//    public void update(User vo){
//
//    }

    public void delete(User vo){
        em.remove(vo);
    }


}
