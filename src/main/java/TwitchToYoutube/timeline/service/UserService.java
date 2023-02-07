package TwitchToYoutube.timeline.service;

import TwitchToYoutube.timeline.entity.User;
import TwitchToYoutube.timeline.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service
public class UserService {
    @Autowired
    private UserRepository repository;

    public User find(Long id){
        return repository.find(id);

    }

    public Long save(User vo){
        return repository.save(vo);
    }
}
