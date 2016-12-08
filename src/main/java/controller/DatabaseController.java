package controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import model.Player;
import model.Rank;
import model.Result;
import model.Track;
import org.hibernate.Session;
import org.hibernate.query.Query;
import utils.HibernateUtil;

import java.util.Date;
import java.util.List;

/**
 * Created by Mateusz on 01.12.2016.
 */
public class DatabaseController {

    private List<Object[]> recordsList;

    public List<Object[]> getAllResults(){
        String query = "select p.playerName, r.result, r.date " +
                "from Player as p " +
                "inner join p.resultSet as r " +
                "order by r.result";

        return getResults(query);
    }


    public List<Object[]> getResultsByTrack(Integer trackId){
       String query =
                "select p.playerName, r.result, r.date " +
                "from Player as p " +
                "inner join p.resultSet as r " +
                "where r.track = " + trackId + " " +
                "order by r.result";

        return getResults(query);
    }




    public void putDataToDB(){
        Session session = HibernateUtil.getSessionFactory().openSession();
        session.beginTransaction();

        Player player = new Player();
        Result result = new Result();
        Track track = new Track();

//        player.setPlayerName("Artur");
//        session.save(player);

//        track.setTrackName("Hard");
//        session.save(track);

        track = session.getSession().load(Track.class, 1);
        player = session.getSession().load(Player.class, 4);

        result.setDate( new Date(new Date().getTime() + (1000 * 60 * 60 * 45)));
        result.setResult(10.142f);
        result.setPlayer(player);
        result.setTrack(track);

        player.getResultSet().add(result);
        track.getResultSet().add(result);

        session.save(result);
        session.getTransaction().commit();

        session.close();
    }

    private List<Object[]> getResults(String query){
        Session session = HibernateUtil.getSessionFactory().openSession();
        session.beginTransaction();

        Query sessionQuery = session.createQuery(query);
        recordsList = sessionQuery.list();

        session.getTransaction().commit();
        return recordsList;
    }

}
