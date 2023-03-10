package TwitchToYoutube.timeline.entity;

import TwitchToYoutube.timeline.enums.AuthType;
import lombok.*;

import javax.persistence.*;

@Entity
@Getter @Setter
@Table(name="MEMBER")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@ToString(of = {"userId","userLogin","userDisplay","userAuth"})
public class User {
    public User(Long userId, String userLogin, String userDisplay, AuthType userAuth) {
        this.userId = userId;
        this.userLogin = userLogin;
        this.userDisplay = userDisplay;
        this.userAuth = userAuth;
    }

    @Id
    @Column(name="user_id")
    private Long userId;

    @Column(name="user_login")
    private String userLogin;

    @Column(name="user_display")
    private String userDisplay;

    @Enumerated(EnumType.STRING)
    @Column(name="user_auth")
    private AuthType userAuth;
}
