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
        String query = "select p.playerName, r.result " +
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

    public List<Object[]> getResultsFromSearchCriteria(String playerName, String trackName){
        String query =  "select p.playerName, r.result " +
                "from Result as r " +
                "inner join r.track as t " +
                "inner join r.player as p " +
                "where p.playerName like '%" + playerName + "%' " +
                "and t.trackName like '%" + trackName + "%' " +
                "order by r.result";
        return getResults(query);
    }

    public void saveResult(String playerName, Long playerResult){

        Player player = new Player();
        Result result = new Result();
        Track track = new Track();

        Session session = HibernateUtil.getSessionFactory().openSession();
        session.beginTransaction();

        if ( checkUsernameExist(playerName) ){
            player = session.getSession().load(Player.class, getPlayerNames().indexOf(playerName));
        } else {
            player.setPlayerName(playerName);
            session.save(player);
        }

        track = session.getSession().load(Track.class, 1);

        result.setResult(playerResult);
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
