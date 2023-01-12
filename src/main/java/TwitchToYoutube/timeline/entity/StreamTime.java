package TwitchToYoutube.timeline.entity;

import lombok.*;

import javax.persistence.*;
import java.util.Date;

@Entity
@Getter
@Table(name="STREAM_TIME")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@ToString(of = {"streamTimeId","streamNick","streamStartTime","streamEndTime","streamSecond"})
public class StreamTime {



    @Id
    @Column(name="streamtime_id")
    @GeneratedValue(strategy = GenerationType.AUTO,
            generator = "streamtime_seq")
    private Long streamTimeId;
    @Column(name = "streamtime_nick")
    private String streamNick;
    @Column(name = "streamtime_start")
    @Temporal(TemporalType.TIMESTAMP)
    private Date streamStartTime;
    @Column(name = "streamtime_end")
    @Temporal(TemporalType.TIMESTAMP)
    private Date streamEndTime;
    @Column(name = "streamtime_sec")
    private Integer streamSecond;

    public StreamTime(String streamNick, Date streamStartTime, Date streamEndTime, Integer streamSecond) {
        this.streamNick = streamNick;
        this.streamStartTime = streamStartTime;
        this.streamEndTime = streamEndTime;
        this.streamSecond = streamSecond;
    }

}