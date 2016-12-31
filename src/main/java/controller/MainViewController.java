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

import java.io.IOException;
import java.time.LocalDate;

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

    public ServerRequestDispatcher serverRequestDispatcher;
    /**
     * Main constructor
     */
    public MainViewController() {
        model = new MainModel();
        tcpServer = TCPServer.getInstance();
        serverModel = ServerModel.getInstance();
        serverRequestDispatcher = new ServerRequestDispatcher();

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

        txtSearchPlayer.textProperty().addListener((observable, oldValue, newValue) -> model.setPlayerListContainsText(newValue));
        testTxtField.textProperty().bindBidirectional(serverModel.getReceivedMessage());
        labelConnection.textProperty().bind(serverModel.getConnection());

        serverModel.getReceivedMessage().addListener((observable, oldValue, newValue) -> {
            if (oldValue != null) {
                if (oldValue.equals(ServerRequest.CONNECT)) {
                    Platform.runLater(() -> serverModel.setConnection(ServerRequest.CONNECTED_DEVICE + newValue));
                    labelConnection.setTextFill(Color.GREEN);
                } else if (newValue.equals(ServerRequest.DISCONNECT)) {
                    Platform.runLater(() -> serverModel.setConnection(ServerRequest.NO_CONNECTION));
                    labelConnection.setTextFill(Color.RED);
                    tcpServer = TCPServer.getInstance();
                }
            }
        });

//        serverRequestDispatcher.requestDispatch();


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
     * @throws IOException
     */
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
