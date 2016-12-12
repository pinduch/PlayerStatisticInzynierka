package controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import model.MainModel;
import model.TableRankData;
import org.hibernate.Session;
import org.hibernate.query.Query;
import utils.HibernateUtil;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by Mateusz on 14.11.2016.
 */
public class MainViewController {

    private MainModel model;

    @FXML
    private TableView<TableRankData> tblRankData;

    @FXML
    private TableColumn<TableRankData, String> tblId, tblName, tblResult, tblDate;

    @FXML
    private Label ipAddressLabel;

    @FXML
    private ComboBox cmbTrackNames;

    @FXML
    private ListView lstPlayerNames;

    @FXML
    private DatePicker calDateFrom;

    @FXML
    private DatePicker calDateTo;

    @FXML
    private TextField testTxtField;

    public TCPServer tcpServer;


    public MainViewController() {
        model = new MainModel();
        tcpServer = TCPServer.getInstance();
    }

    @FXML
    private void initialize()
    {

        tblId.setCellValueFactory(new PropertyValueFactory<TableRankData, String>("id"));
        tblId.prefWidthProperty().bind(tblRankData.widthProperty().multiply(0.10));
        tblName.setCellValueFactory(new PropertyValueFactory<TableRankData, String>("name"));
        tblName.prefWidthProperty().bind(tblRankData.widthProperty().multiply(0.30));
        tblResult.setCellValueFactory(new PropertyValueFactory<TableRankData, String>("result"));
        tblResult.prefWidthProperty().bind(tblRankData.widthProperty().multiply(0.30));
        tblDate.setCellValueFactory(new PropertyValueFactory<TableRankData, String>("date"));
        tblDate.prefWidthProperty().bind(tblRankData.widthProperty().multiply(0.30));

//        lstPlayerNames.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);

        ipAddressLabel.setText(model.getIpAddress());
        cmbTrackNames.setItems(model.getCmbTrackList());
        lstPlayerNames.setItems(model.getLstPlayerList());
        tblRankData.setItems(model.getTableRankDataList());

        cmbTrackNames.getSelectionModel().selectFirst();
        lstPlayerNames.getSelectionModel().selectFirst();

  //      database.putDataToDB();


        try {
            startServer();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    /**
     * Method which get all input data from search criteria and show in table results of this criteria.
     */
    public void searchResults(){
        String playerName = lstPlayerNames.getSelectionModel().getSelectedItem().toString();
        String trackName = cmbTrackNames.getSelectionModel().getSelectedItem().toString();
        LocalDate dateFrom = calDateFrom.getValue();
        LocalDate dateTo = calDateTo.getValue();

        if (lstPlayerNames.getSelectionModel().getSelectedIndex() == 0) playerName = "";
        if (cmbTrackNames.getSelectionModel().getSelectedIndex() == 0) trackName = "";

        model.setTableRankDataFromSearchCriteria(playerName, trackName,dateFrom, dateTo);
    }

    public void clearSearchCrietria(){
        cmbTrackNames.getSelectionModel().selectFirst();
        lstPlayerNames.getSelectionModel().selectFirst();
        calDateFrom.getEditor().clear();
        calDateFrom.setValue(null);
        calDateTo.getEditor().clear();
        calDateTo.setValue(null);
    }


    public void startServer() throws IOException {

//        tcpServer = new TCPServer(txtArea);
//        txtMainText.setText("Try to connect to server");
        tcpServer.start();

    }

    public void sendMessage() {

//        tcpServer.sendMessage(txtMessage.getText());
//        txtMessage.clear();

    }


}
