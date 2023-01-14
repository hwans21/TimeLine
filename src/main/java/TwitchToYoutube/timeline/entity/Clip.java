package TwitchToYoutube.timeline.entity;

import lombok.*;

import javax.persistence.*;
import java.util.Date;

@Entity
@Getter
@Table(name="CLIP")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@ToString(of = {"clipId","clipWriter","clipTitle","clipTime","clipCount"})
public class Clip {

    public Clip(String clipWriter, String clipTitle, Date clipTime, Integer clipCount, StreamCheck stream, Record record) {
        this.clipWriter = clipWriter;
        this.clipTitle = clipTitle;
        this.clipTime = clipTime;
        this.clipCount = clipCount;
        this.stream = stream;
        this.record = record;
    }
    public void setRecord(Record record){
        this.record = record;
    }

    @Id
    @Column(name="clip_id")
    @GeneratedValue(strategy = GenerationType.AUTO,
                generator = "clip_seq")
    private Long clipId;

    @Column(name="clip_writer")
    private String clipWriter;

    @Column(name="clip_title")
    private String clipTitle;

    @Column(name="clip_time")
    @Temporal(TemporalType.TIMESTAMP)
    private Date clipTime;

    @Column(name="clip_count")
    private Integer clipCount;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="stream_id")
    private StreamCheck stream;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "record_id")
    private Record record;

}
