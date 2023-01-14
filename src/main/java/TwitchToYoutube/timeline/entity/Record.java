package TwitchToYoutube.timeline.entity;

import lombok.*;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Entity
@Getter @Setter
@Table(name="RECORD")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@ToString(of = {"recordId", "recordStart","recordSecond","recordUrl"})
public class Record {

    public Record(Date recordStart, Integer recordSecond, String recordUrl, StreamCheck stream) {
        this.recordStart = recordStart;
        this.recordSecond = recordSecond;
        this.recordUrl = recordUrl;
        this.stream = stream;
    }

    @Id
    @Column(name="record_id")
    @GeneratedValue(strategy = GenerationType.AUTO,
                    generator = "record_seq")
    private Long recordId;

    @Column(name = "record_start")
    @Temporal(TemporalType.TIMESTAMP)
    private Date recordStart;

    @Column(name = "record_second")
    private Integer recordSecond;

    @Column(name = "record_url")
    private String recordUrl;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="stream_id")
    private StreamCheck stream;

    @OneToMany(mappedBy = "record")
    private List<Clip> clips = new ArrayList<>();

}
