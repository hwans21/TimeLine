package TwitchToYoutube.timeline.entity;

import lombok.*;

import javax.persistence.*;
import java.util.Date;

@Entity
@Getter
@Table(name="TIMELINE")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@ToString(of = {"timelineId","userId","timelineTitle","timelineTime","timelineCount"})
public class Timeline {


    public Timeline(User userId, String timelineTitle, Date timelineTime, Long timelineCount) {
        this.userId = userId;
        this.timelineTitle = timelineTitle;
        this.timelineTime = timelineTime;
        this.timelineCount = timelineCount;
    }

    @Id
    @Column(name="timeline_id")
    @GeneratedValue(strategy = GenerationType.AUTO,
            generator = "timeline_seq")
    private Long timelineId;

    @Column(name="timeline_title")
    private String timelineTitle;

    @Column(name="timeline_time")
    private Date timelineTime;

    @Column(name="timeline_count")
    private Long timelineCount;

    @ManyToOne
    @JoinColumn(name="user_id")
    private User userId;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name="youtube_id")
    private Youtube youtubeId;

    @Setter
    @Transient
    private int timelineSec;

    @Setter
    @Transient
    private String timeFormat;



}
