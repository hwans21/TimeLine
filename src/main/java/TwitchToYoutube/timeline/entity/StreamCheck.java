package TwitchToYoutube.timeline.entity;

import lombok.*;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Entity
@Getter
@Table(name="STREAM")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@ToString(of = {"streamId","streamNick","streamStart","streamSecond"})
public class StreamCheck {


    public StreamCheck(String streamNick, Date streamStart, Integer streamSecond) {
        this.streamNick = streamNick;
        this.streamStart = streamStart;
        this.streamSecond = streamSecond;
    }

    @Id
    @Column(name="stream_id")
    @GeneratedValue(strategy = GenerationType.AUTO,
            generator = "stream_seq")
    private Long streamId;

    @Column(name = "stream_nick")
    private String streamNick;

    @Column(name = "stream_start")
    @Temporal(TemporalType.TIMESTAMP)
    private Date streamStart;

    @Column(name = "stream_sec")
    private Integer streamSecond;

    @OneToMany(mappedBy = "stream")
    private List<Record> records = new ArrayList<>();

    @OneToMany(mappedBy = "stream")
    private List<Clip> clips = new ArrayList<>();

}