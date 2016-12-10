package entity;

import javax.persistence.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by Mateusz on 30.11.2016.
 */

@Entity
@Table
public class Track implements Serializable {

    private Integer id;
    private String trackName;
    private Set<Result> resultSet = new HashSet<Result>(0);


    public Track(){

    }

    public Track(String trackName){
        this.trackName = trackName;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "TRACK_ID")
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    @Column(name = "TRACK_NAME")
    public String getTrackName() {
        return trackName;
    }

    public void setTrackName(String trackName) {
        this.trackName = trackName;
    }

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "track")
    public Set<Result> getResultSet() {
        return resultSet;
    }

    public void setResultSet(Set<Result> resultSet) {
        this.resultSet = resultSet;
    }
}
