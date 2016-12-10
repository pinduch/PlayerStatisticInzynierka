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
public class Player implements Serializable {

    private Integer id;
    private String playerName;
    private Set<Result> resultSet = new HashSet<Result>(0);

    public Player(){

    }

    public Player(String playerName){
        this.playerName = playerName;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "PLAYER_ID")
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    @Column(name = "PLAYER_NAME")
    public String getPlayerName() {
        return playerName;
    }

    public void setPlayerName(String playerName) {
        this.playerName = playerName;
    }

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "player")
    public Set<Result> getResultSet() {
        return resultSet;
    }

    public void setResultSet(Set<Result> resultSet) {
        this.resultSet = resultSet;
    }
}
