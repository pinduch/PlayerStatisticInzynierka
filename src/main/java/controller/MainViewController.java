package controller;

import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.event.EventType;
import org.hibernate.SessionFactory;
import org.hibernate.boot.model.relational.Database;
import utils.HibernateUtil;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import model.Player;
import model.Rank;
import model.Result;
import model.Track;
import org.hibernate.Session;
import org.hibernate.query.Query;

import java.io.IOException;
import java.net.Inet4Address;
import java.net.UnknownHostException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Set;

/**
 * Created by Mateusz on 14.11.2016.
 */
public class MainViewController {

//------------------------  JavaFX elements -------------------------------
    @FXML
    private Button btnSend;

    @FXML
    private TextField txtMessage;

    @FXML
    private Label txtMainText;

    @FXML
    private TextArea txtArea;

    @FXML
    private TableView<Rank> rankTbl;

    @FXML
    private TableColumn<Rank, String> tblId, tblName, tblResult, tblDate;

    @FXML
    private Label ipAddressLabel;

    @FXML
    private ComboBox cmbTrack;

    @FXML
    private ComboBox cmbPlayer;

    @FXML
    private Button btnSearch;

//----------------------- Other variables --------------------------------
    public TCPServer tcpServer;
    public DatabaseController database = new DatabaseController();



    @FXML
    private void initialize()
    {
        tblId.setCellValueFactory(new PropertyValueFactory<Rank, String>("id"));
        tblId.prefWidthProperty().bind(rankTbl.widthProperty().multiply(0.10));
        tblName.setCellValueFactory(new PropertyValueFactory<Rank, String>("name"));
        tblName.prefWidthProperty().bind(rankTbl.widthProperty().multiply(0.30));
        tblResult.setCellValueFactory(new PropertyValueFactory<Rank, String>("result"));
        tblResult.prefWidthProperty().bind(rankTbl.widthProperty().multiply(0.30));
        tblDate.setCellValueFactory(new PropertyValueFactory<Rank, String>("date"));
        tblDate.prefWidthProperty().bind(rankTbl.widthProperty().multiply(0.30));

        ElementInitializer.setIpAddress(ipAddressLabel);
        ElementInitializer.setComboBoxItems(cmbTrack, database.getTrackNames());
        ElementInitializer.setComboBoxItems(cmbPlayer, database.getPlayerNames());

        setRankTable(database.getAllResults());

// TODO:
//      przy tym się pierdoli.... nie wiem jak naprawić ;/
//        cmbTrack.getEditor().textProperty().addListener((observable, oldValue, newValue) -> {
//            searchCombobox(newValue);
//        });

  //      database.putDataToDB();


    }


    /**
     * Method which get all input data from search criteria and show in table results of this criteria.
     */
    public void searchResults(){
        Object playerName = cmbPlayer.getSelectionModel().getSelectedItem();
        Object trackName = cmbTrack.getSelectionModel().getSelectedItem();

        String player = "";
        String track = "";

        if (playerName != null) player = playerName.toString();
        if (trackName != null) track = trackName.toString();

        setRankTable(database.getResultsFromSearchCriteria(player, track));
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

    public void startServer() throws IOException {

        tcpServer = new TCPServer(txtArea);
        txtMainText.setText("Try to connect to server");
        tcpServer.start();

    }

    public void sendMessage() {

        tcpServer.sendMessage(txtMessage.getText());
        txtMessage.clear();

    }

    /**
     * Method to set data in rank table.
     *
     * @param resultList List of Objects received from database
     */
    public void setRankTable(List<Object[]> resultList){
        Integer i = 1;
        ObservableList<Rank> data = FXCollections.observableArrayList();
        for (Object[] result : resultList){
            Rank rank = new Rank(
                    i.toString(),
                    String.valueOf(result[0]),
                    String.valueOf(result[1]),
                    changeDate(String.valueOf(result[2]), "HH:mm '/' dd.MM.yy")
            );
            data.add(rank);
            i++;
        }

        rankTbl.setItems(data);
    }

    // póki co to nie działa leci exception IndexOutOfBound jeśli zmienię zawartość observableList i coś wybiorę
    // jak to dodałęm poprzez metode tak jak teraz to po wyborze jakiejś wartości wgl nie mogę jej zmienić....
    public void searchCombobox(String value){
        ObservableList<String> tracksList = FXCollections.observableArrayList();

        for (Object result : database.getTrackNamesContainsText(value.trim())) {
            tracksList.add(String.valueOf(result));
        }

        cmbTrack.setItems(tracksList);
        cmbTrack.show();
    }


}
