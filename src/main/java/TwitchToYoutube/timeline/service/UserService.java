package TwitchToYoutube.timeline.service;

import TwitchToYoutube.timeline.entity.User;
import TwitchToYoutube.timeline.repository.UserRepository;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service
public class UserService {
    private final Logger log = LogManager.getLogger(this.getClass());

    @Autowired
    private UserRepository repository;

    public User find(Long id){
        log.debug("User Find Service");
        return repository.find(id);

    }

    public Long save(User vo){
        log.debug("User Insert Service");
        return repository.save(vo);
    }
}
