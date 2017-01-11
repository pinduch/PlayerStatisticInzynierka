package controller;

import common.Constant;
import common.ServerRequest;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.paint.Color;
import model.MainModel;
import model.ServerModel;
import model.TableRankData;

import java.util.Optional;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.TimeUnit;

/**
 * Created by Mateusz on 14.11.2016.
 */
public class MainViewController {

    private MainModel model;

    @FXML
    private TableView<TableRankData> tblRankData;

    @FXML
    private TableColumn<TableRankData, String> tblId, tblName, tblResult;

    @FXML
    private Label ipAddressLabel;

    @FXML
    private ComboBox cmbTrackNames;

    @FXML
    private ListView lstPlayerNames;

    @FXML
    private TextField txtSearchPlayer;

    @FXML
    private Label labelConnection;

    @FXML
    private Label labelResult;

    public TCPServer tcpServer;
    public ServerModel serverModel;
    public DatabaseController database;


    private long startTime = 0L;
    private long elapsedTimeInMilliseconds = 0L;
    private String elapsedTime = "0.0";
    private Timer timer;
    private  long min, sec, millis;

    /**
     * Main constructor
     */
    public MainViewController() {
        model = new MainModel();
        serverModel = ServerModel.getInstance();
        database = new DatabaseController();
    }

    @FXML
    private void initialize()
    {
        tblId.setCellValueFactory(new PropertyValueFactory<TableRankData, String>("id"));
        tblId.prefWidthProperty().bind(tblRankData.widthProperty().multiply(0.20));
        tblName.setCellValueFactory(new PropertyValueFactory<TableRankData, String>("name"));
        tblName.prefWidthProperty().bind(tblRankData.widthProperty().multiply(0.40));
        tblResult.setCellValueFactory(new PropertyValueFactory<TableRankData, String>("result"));
        tblResult.prefWidthProperty().bind(tblRankData.widthProperty().multiply(0.36));

//        lstPlayerNames.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);

        ipAddressLabel.setText(model.getIpAddress());
        cmbTrackNames.setItems(model.getCmbTrackList());
        lstPlayerNames.setItems(model.getLstPlayerList());
        tblRankData.setItems(model.getTableRankDataList());

        cmbTrackNames.getSelectionModel().selectFirst();
        lstPlayerNames.getSelectionModel().selectFirst();
  //      database.putDataToDB();

        txtSearchPlayer.textProperty().addListener((observable, oldValue, newValue) -> model.setPlayerListContainsText(newValue));
        labelConnection.textProperty().bind(serverModel.getConnection());
        labelResult.textProperty().bind(model.resultPlayerTimeProperty());

        serverModel.getReceivedMessage().addListener(
                (observable, oldValue, newValue) -> serverRequestDispatcherListener(oldValue,newValue)
        );

        startServer();
    }


    /**
     * Method which get all input data from search criteria and show in table results of this criteria.
     */
    public void searchResults(){
        String playerName = lstPlayerNames.getSelectionModel().getSelectedItem().toString();
        String trackName = cmbTrackNames.getSelectionModel().getSelectedItem().toString();

        if (lstPlayerNames.getSelectionModel().getSelectedIndex() == 0) playerName = "";
        if (cmbTrackNames.getSelectionModel().getSelectedIndex() == 0) trackName = "";

        model.setTableRankDataFromSearchCriteria(playerName, trackName);

    }

    /**
     * Method to clear provided search criteria
     */
    public void clearSearchCriteria(){
        cmbTrackNames.getSelectionModel().selectFirst();
        lstPlayerNames.getSelectionModel().selectFirst();
        txtSearchPlayer.clear();
    }


    /**
     * Method to start server TCP
     */
    public void startServer() {
        tcpServer = TCPServer.getInstance();
        tcpServer.start();
    }


    private void serverRequestDispatcherListener(String oldValue, String newValue){

        if (ServerRequest.fieldsValue().contains(newValue)){
            switch (newValue){
                case ServerRequest.DISCONNECT:
                    Platform.runLater(() -> serverModel.setConnection(ServerRequest.NO_CONNECTION));
                    labelConnection.setTextFill(Color.RED);
                    startServer();
                    break;
                case ServerRequest.START_RACE:
                    startTimer();
                    break;
                case ServerRequest.END_RACE:
                    stopTimer();
                    Platform.runLater(() -> showAlert(elapsedTime));
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
                case ServerRequest.USERNAME:
                    model.setActualUsername(newValue);
                    break;
            }


        }

    }

    private void showAlert(String playerResult){
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle(Constant.DIALOG_TITLE);
        alert.setHeaderText("RESULT: " + playerResult);
        alert.setContentText(Constant.DIALOG_MESSAGE);

        DialogPane dialogPane = alert.getDialogPane();
        dialogPane.getStylesheets().add("myStylesheet.css");

        Optional<ButtonType> result = alert.showAndWait();
        if (result.get() == ButtonType.OK){
            database.saveResult(model.getActualUsername(), elapsedTimeInMilliseconds);
            searchResults();
            model.refreshPlayerList();
            model.setResultPlayerTime(Constant.START_LABEL_RESULT);
        } else {
            model.setResultPlayerTime(Constant.START_LABEL_RESULT);
        }
    }

    private void startTimer(){
        timer = new Timer();
        startTime = System.currentTimeMillis();
        new Thread(() -> timer.schedule(new TimerTask() {
            @Override
            public void run() {
                elapsedTimeInMilliseconds = System.currentTimeMillis() - startTime;

                min = TimeUnit.MILLISECONDS.toMinutes(elapsedTimeInMilliseconds);
                sec = TimeUnit.MILLISECONDS.toSeconds(elapsedTimeInMilliseconds) - TimeUnit.MINUTES.toSeconds(min);
                millis = elapsedTimeInMilliseconds - TimeUnit.MINUTES.toMillis(min) - TimeUnit.SECONDS.toMillis(sec);

                elapsedTime = String.format("%02d.%02d.%03d", min, sec, millis);

                Platform.runLater(() -> model.setResultPlayerTime(
                        "PLAYER: " + model.getActualUsername() + "\t\tRESULT: " + elapsedTime)
                );
            }
        }, 0, 1)).start();

    }

    private void stopTimer(){
        timer.cancel();
        timer.purge();
        Platform.runLater(() ->
                model.setResultPlayerTime("PLAYER: " + model.getActualUsername() + "\t\tRESULT: " + elapsedTime)
        );
    }

}
