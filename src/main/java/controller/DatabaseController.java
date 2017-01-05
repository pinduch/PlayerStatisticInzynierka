package controller;

import common.Constant;
import entity.Player;
import entity.Result;
import entity.Track;
import org.hibernate.Session;
import org.hibernate.query.Query;
import utils.HibernateUtil;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by Mateusz on 01.12.2016.
 */
public class DatabaseController {

    private List<Object[]> recordsList;

    /**
     * Get all results from database.
     *
     * @return
     */
    public List<Object[]> getAllResults(){
        String query = "select p.playerName, r.result, r.date " +
                "from Player as p " +
                "inner join p.resultSet as r " +
                "order by r.result";

        return getResults(query);
    }



    /**
     * Get name of all Tracks.
     * @return
     */
    public List<Object[]> getTrackNames(){
        String query =  "select t.trackName " +
                        "from Track as t";
        return getResults(query);
    }

    /**
     * Get name of all Players.
     *
     * @return
     */
    public List<Object[]> getPlayerNames(){
        String query =  "select p.playerName " +
                "from Player as p " +
                "order by p.playerName";
        return getResults(query);
    }

    /**
     * Get all track names which contains text.
     *
     * @param text
     * @return
     */
    public List<Object[]> getPlayerNamesContainsText(String text){
        String query =  "select p.playerName " +
                        "from Player as p " +
                        "where p.playerName like '%" + text + "%'";
        return getResults(query);
    }

    public boolean checkUsernameExist(String username){
        String query = "select p.playerName " +
                       "from Player as p " +
                       "where p.playerName = '" + username + "'";
        if (getResults(query).isEmpty()) return false;
            else return true;
    }

    public List<Object[]> getResultsFromSearchCriteria(String playerName, String trackName, LocalDate dateFrom, LocalDate dateTo){
        Date dateFromParameter = null;
        Date dateToParameter = null;

        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        try {
            if (dateFrom != null)
                dateFromParameter = format.parse(dateFrom + " 00:00:00");
            if (dateTo != null)
                dateToParameter = format.parse(dateTo + " 23:59:59");
        } catch (ParseException ex){
            ex.printStackTrace();
        }

        String query =  "select p.playerName, r.result, r.date " +
                        "from Result as r " +
                        "inner join r.track as t " +
                        "inner join r.player as p " +
                        "where p.playerName like '%" + playerName + "%' " +
                        "and t.trackName like '%" + trackName + "%' ";
        if (dateFrom != null) {
            query += "and r.date > :" + Constant.DATE_FROM + " ";
        }
        if (dateTo != null) {
            query += "and r.date < :" + Constant.DATE_TO + " ";
        }
            query += "order by r.result";

        return getResults(query, dateFromParameter, dateToParameter);
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
        player = session.getSession().load(Player.class, 1);

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

    /**
     * Private method to get objects from database.
     *
     * @param query specific query that is send to database
     * @return query results
     */
    private List<Object[]> getResults(String query){
        Session session = HibernateUtil.getSessionFactory().openSession();
        session.beginTransaction();

        Query sessionQuery = session.createQuery(query);
        recordsList = sessionQuery.list();

        session.getTransaction().commit();
        return recordsList;
    }

    /**
     * Method to get results from database.
     *
     * @param query specific query which is send to database
     * @param dateFrom parameter used in query
     * @param dateTo parameter used in query
     * @return
     */
    private List<Object[]> getResults(String query, Date dateFrom, Date dateTo){
        Session session = HibernateUtil.getSessionFactory().openSession();
        session.beginTransaction();

        Query sessionQuery = session.createQuery(query);
        if (dateFrom != null ) {
            sessionQuery.setParameter(Constant.DATE_FROM, dateFrom);
        }
        if (dateTo != null ) {
            sessionQuery.setParameter(Constant.DATE_TO, dateTo);
        }

        recordsList = sessionQuery.list();

        session.getTransaction().commit();
        return recordsList;
    }

}
