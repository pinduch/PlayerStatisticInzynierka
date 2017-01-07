package entity;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

/**
 * Created by Mateusz on 30.11.2016.
 */

@Entity
@Table
public class Result implements Serializable {

    private Integer id;
    private Long result;
    private Track track;
    private Player player;


    public Result(){

    }


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID")
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    @Column(name = "RESULT")
    public Long getResult() {
        return result;
    }

    public void setResult(Long result) {
        this.result = result;
    }

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "PLAYER_ID")
    public Player getPlayer() {
        return player;
    }

    public void setPlayer(Player player) {
        this.player = player;
    }

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "TRACK_ID")
    public Track getTrack() {
        return track;
    }

    public void setTrack(Track track) {
        this.track = track;
    }
}
