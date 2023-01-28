package TwitchToYoutube.timeline.entity;

import TwitchToYoutube.timeline.enums.AuthType;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.*;
import java.util.Date;

@Entity
@Getter
@Table(name="YOUTUBE")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@ToString(of = {"youtubeId","userId","youtubeTitle","youtubeRecordStart","youtubeLength","youtubeURL"})
public class Youtube {


    public Youtube(Long userId, String youtubeTitle, Date youtubeRecordStart, String youtubeLength, String youtubeUrl) {
        this.userId = userId;
        this.youtubeTitle = youtubeTitle;
        this.youtubeRecordStart = youtubeRecordStart;
        this.youtubeLength = youtubeLength;
        this.youtubeUrl = youtubeUrl;
    }


    @Id
    @Column(name="youtube_id")
    @GeneratedValue(strategy = GenerationType.AUTO,
                generator = "youtube_seq")
    private Long youtubeId;

    @Column(name="user_id")
    private Long userId;

    @Column(name="youtube_title")
    private String youtubeTitle;

    @Column(name="youtube_record_start")
    @Temporal(TemporalType.TIMESTAMP)
    private Date youtubeRecordStart;


    @Column(name="youtube_length")
    private String youtubeLength;

    @Column(name="youtube_url")
    private String youtubeUrl;
}
