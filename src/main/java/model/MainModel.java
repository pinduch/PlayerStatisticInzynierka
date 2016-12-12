package model;

import common.Constant;
import controller.DatabaseController;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.io.IOException;
import java.net.Inet4Address;
import java.net.UnknownHostException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.Date;
import java.util.List;
import java.util.stream.Collector;
import java.util.stream.Collectors;

/**
 * Created by Mateusz on 09.12.2016.
 */
public class MainModel {

    private DatabaseController database;
    private ObservableList<String> cmbTrackList;
    private ObservableList<String> lstPlayerList;
    private ObservableList<TableRankData> tableRankDataList;
    private String ipAddress;

    public MainModel() {
        database = new DatabaseController();

        cmbTrackList = FXCollections.observableArrayList();
        lstPlayerList = FXCollections.observableArrayList();
        tableRankDataList = FXCollections.observableArrayList();

        setTrackCombo();
        setPlayerList();
        setIpAddress();
        setTableRankData();
    }


    public ObservableList<String> getCmbTrackList() {
        return cmbTrackList;
    }

    public void setCmbTrackList(ObservableList<String> cmbTrackList) {
        this.cmbTrackList = cmbTrackList;
    }

    public ObservableList<String> getLstPlayerList() {
        return lstPlayerList;
    }

    public void setLstPlayerList(ObservableList<String> lstPlayerList) {
        this.lstPlayerList = lstPlayerList;
    }

    public ObservableList<TableRankData> getTableRankDataList() {
        return tableRankDataList;
    }

    public void setTableRankDataList(ObservableList<TableRankData> tableRankDataList) {
        this.tableRankDataList = tableRankDataList;
    }

    public String getIpAddress() {
        return ipAddress;
    }

    public void setIpAddress(String ipAddress) {
        this.ipAddress = ipAddress;
    }

    public void setTableRankDataFromSearchCriteria(String playerName, String trackName, LocalDate dateFrom, LocalDate dateTo){
        setTableRankData(database.getResultsFromSearchCriteria(playerName, trackName, dateFrom, dateTo));
    }

    /**
     * Method to change date format which is show in rank table
     *
     * @param oldDate date in String
     * @param dateFormat new date format
     * @return
     */
    public String changeDate(String oldDate, String dateFormat){
        String newDate = null;
        try {
            Date date = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.S").parse(oldDate);
            newDate = new SimpleDateFormat(dateFormat).format(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return newDate;
    }


    private void setTrackCombo() {
        cmbTrackList.add(Constant.ALL_TRACKS);
        for (Object result : database.getTrackNames()) {
            cmbTrackList.add(String.valueOf(result));
        }
    }

    private void setPlayerList(){
        lstPlayerList.add(Constant.ALL_PLAYERS);
        for (Object result : database.getPlayerNames()){
            lstPlayerList.add(String.valueOf(result));
        }
    }

    private void setIpAddress(){
        try {
            ipAddress = Inet4Address.getLocalHost().getHostAddress();
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
    }

    private void setTableRankData(){
        setTableRankData(database.getAllResults());
    }

    private void setTableRankData(List<Object[]> resultList){
        tableRankDataList.clear();
        Integer i = 1;
        for (Object[] result : resultList) {
            TableRankData tableRankData = new TableRankData(
                    i.toString(),
                    String.valueOf(result[0]),
                    String.valueOf(result[1]),
                    changeDate(String.valueOf(result[2]), "HH:mm \ndd.MM.yy")
            );
            tableRankDataList.add(tableRankData);
            i++;
        }
    }
}
