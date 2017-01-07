package model;

import common.Constant;
import controller.DatabaseController;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
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
import java.util.concurrent.TimeUnit;
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
    private String actualUsername;
    private StringProperty resultPlayerTime;

    public MainModel() {
        database = new DatabaseController();

        cmbTrackList = FXCollections.observableArrayList();
        lstPlayerList = FXCollections.observableArrayList();
        tableRankDataList = FXCollections.observableArrayList();

        setTrackCombo();
        setPlayerList();
        setIpAddress();
        setTableRankData();

        resultPlayerTime = new SimpleStringProperty();
        setResultPlayerTime(Constant.START_LABEL_RESULT);
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

    public void setTableRankDataFromSearchCriteria(String playerName, String trackName){
        setTableRankData(database.getResultsFromSearchCriteria(playerName, trackName));
    }

    public StringProperty resultPlayerTimeProperty() {
        return resultPlayerTime;
    }

    public void setResultPlayerTime(String resultPlayerTime) {
        this.resultPlayerTime.set(resultPlayerTime);
    }

    public String getActualUsername() {
        return actualUsername;
    }

    public void setActualUsername(String actualUsername) {
        this.actualUsername = actualUsername;
    }


    public void setPlayerListContainsText(String text){
        lstPlayerList.clear();
        lstPlayerList.add(Constant.ALL_PLAYERS);
        for (Object result : database.getPlayerNamesContainsText(text)){
            lstPlayerList.add(String.valueOf(result));
        }
    }

    public void refreshPlayerList(){
        lstPlayerList.clear();
        setPlayerList();
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
                    convertStringMillisToResult(String.valueOf(result[1]))
            );
            tableRankDataList.add(tableRankData);
            i++;
        }
    }

    private String convertStringMillisToResult(String value){
        String result;

        long min = TimeUnit.MILLISECONDS.toMinutes(Long.valueOf(value));
        long sec = TimeUnit.MILLISECONDS.toSeconds(Long.valueOf(value)) - TimeUnit.MINUTES.toSeconds(min);
        long millis = Long.valueOf(value) - TimeUnit.MINUTES.toMillis(min) - TimeUnit.SECONDS.toMillis(sec);

        result = String.format("%02d.%02d.%03d", min, sec, millis);

        return result;
    }
}
