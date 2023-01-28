package TwitchToYoutube.timeline.entity;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.*;
import java.util.Date;

@Entity
@Getter
@Table(name="TIMELINE")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@ToString(of = {"timelineId","userId","timelineTitle","timelineTime","timelineCount","timelineSec"})
public class Timeline {


    public Timeline(Long userId, String timelineTitle, Date timelineTime, Long timelineCount, Long timelineSec) {
        this.userId = userId;
        this.timelineTitle = timelineTitle;
        this.timelineTime = timelineTime;
        this.timelineCount = timelineCount;
        this.timelineSec = timelineSec;
    }

    @Id
    @Column(name="timeline_id")
    @GeneratedValue(strategy = GenerationType.AUTO,
            generator = "timeline_seq")
    private Long timelineId;

    @Column(name="user_id")
    private Long userId;

    @Column(name="timeline_title")
    private String timelineTitle;

    @Column(name="timeline_time")
    private Date timelineTime;

    @Column(name="timeline_count")
    private Long timelineCount;

    @Column(name="timeline_sec")
    private Long timelineSec;


}