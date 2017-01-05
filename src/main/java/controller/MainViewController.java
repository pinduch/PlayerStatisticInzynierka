package controller;

import common.Constant;
import common.ServerRequest;
import javafx.application.Platform;
import javafx.beans.binding.Bindings;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.paint.Color;
import model.MainModel;
import model.ServerModel;
import model.TableRankData;

import java.io.IOException;
import java.lang.reflect.Field;
import java.time.LocalDate;
import java.util.Arrays;

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
    private TextField txtSearchPlayer;

    @FXML
    private Label labelConnection;

    @FXML
    private TextField testTxtField;

    public TCPServer tcpServer;
    public ServerModel serverModel;
    public DatabaseController database;

    public ServerRequestDispatcher serverRequestDispatcher;
    /**
     * Main constructor
     */
    public MainViewController() {
        model = new MainModel();
        serverModel = ServerModel.getInstance();
        serverRequestDispatcher = new ServerRequestDispatcher();
        database = new DatabaseController();
    }

    @FXML
    private void initialize()
    {
        ServerRequest.fieldsValue().forEach(System.out::println);

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

        txtSearchPlayer.textProperty().addListener((observable, oldValue, newValue) -> model.setPlayerListContainsText(newValue));
        testTxtField.textProperty().bindBidirectional(serverModel.getReceivedMessage());
        labelConnection.textProperty().bind(serverModel.getConnection());

        serverModel.getReceivedMessage().addListener(
                (observable, oldValue, newValue) -> serverRequestDispatcherListener(oldValue,newValue));
//            if (ServerRequest.fieldsValue().contains(oldValue)) {
//                serverRequestDispatcher.requestDispatch(oldValue, newValue);
//            }
//            if (newValue.equals(ServerRequest.DISCONNECT)){
//                serverRequestDispatcher.requestDispatch(newValue, null);
//                startServer();
//            }
//            if (oldValue != null) {
//
//                //TODO pomysł -> oldValue to bedzie to co mam zrobic w newValue przesle w jednym stringu wszystko co chce
//
//                if (oldValue.equals(ServerRequest.CONNECT)) {
//                    Platform.runLater(() -> serverModel.setConnection(ServerRequest.CONNECTED_DEVICE + newValue));
//                    labelConnection.setTextFill(Color.GREEN);
//                } else if (newValue.equals(ServerRequest.DISCONNECT)) {
//                    Platform.runLater(() -> serverModel.setConnection(ServerRequest.NO_CONNECTION));
//                    labelConnection.setTextFill(Color.RED);
//                    startServer();
//                } else {
//                    serverRequestDispatcher.requestDispatch(newValue, newValue);
//                }
//            }
//        });

//        serverRequestDispatcher.requestDispatch();

        startServer();
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

    /**
     * Method to clear provided search criteria
     */
    public void clearSearchCriteria(){
        cmbTrackNames.getSelectionModel().selectFirst();
        lstPlayerNames.getSelectionModel().selectFirst();
        calDateFrom.getEditor().clear();
        calDateFrom.setValue(null);
        calDateTo.getEditor().clear();
        calDateTo.setValue(null);
        txtSearchPlayer.clear();
    }


    /**
     * Method to start server TCP
     */
    public void startServer() {
//        tcpServer = new TCPServer(txtArea);
//        txtMainText.setText("Try to connect to server");
        tcpServer = TCPServer.getInstance();
        tcpServer.start();
    }

    public void sendMessage() {

//        tcpServer.sendMessage(txtMessage.getText());
//        txtMessage.clear();

    }

    private void serverRequestDispatcherListener(String oldValue, String newValue){

        if (ServerRequest.fieldsValue().contains(newValue)){
            switch (newValue){
                case ServerRequest.DISCONNECT:
                    Platform.runLater(() -> serverModel.setConnection(ServerRequest.NO_CONNECTION));
                    labelConnection.setTextFill(Color.RED);
                    startServer();
                    break;
            }
        }

        if (ServerRequest.fieldsValue().contains(oldValue)){
            switch (oldValue){
                case ServerRequest.CONNECT:
                    Platform.runLater(() -> serverModel.setConnection(ServerRequest.CONNECTED_DEVICE + newValue));
                    labelConnection.setTextFill(Color.GREEN);
                    break;
                case ServerRequest.CHECK_USERNAME:
                    if (database.checkUsernameExist(newValue)) tcpServer.sendMessage(ServerRequest.TRUE);
                    else tcpServer.sendMessage(ServerRequest.FALSE);
                    break;
            }


        }

//        if (ServerRequest.fieldsValue().contains(oldValue)) {
//            serverRequestDispatcher.requestDispatch(oldValue, newValue);
//        }
//        if (newValue.equals(ServerRequest.DISCONNECT)){
//            serverRequestDispatcher.requestDispatch(newValue, null);
//            startServer();
//        }
    }


}
