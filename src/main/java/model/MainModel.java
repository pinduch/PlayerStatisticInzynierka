package model;

import controller.DatabaseController;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.util.stream.Collectors;

/**
 * Created by Mateusz on 09.12.2016.
 */
public class MainModel {

    private DatabaseController database;
    private ObservableList<String> cmbTrackList;
    private ObservableList<String> lstPlayerList;

    public MainModel() {
        database = new DatabaseController();
        cmbTrackList = FXCollections.observableArrayList();
        lstPlayerList = FXCollections.observableArrayList();
        setTrackCombo();
        setPlayerList();
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


    private void setTrackCombo() {
        cmbTrackList.add("All Tracks");
        for (Object result : database.getTrackNames()) {
            cmbTrackList.add(String.valueOf(result));
        }
//        cmbTrackList.addAll(database.getTrackNames().stream().map(String::valueOf).collect(Collectors.toList()));
    }

    private void setPlayerList(){
        lstPlayerList.add("All Players");
        for (Object result : database.getPlayerNames()){
            lstPlayerList.add(String.valueOf(result));
        }
//        lstPlayerList.addAll(database.getPlayerNames().stream().map(String::valueOf).collect(Collectors.toList()));
    }
}
