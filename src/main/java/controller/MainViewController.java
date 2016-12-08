package controller;

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

        try {
            ipAddressLabel.setText(Inet4Address.getLocalHost().getHostAddress());
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }


  //      database.putDataToDB();

        Session session = HibernateUtil.getSessionFactory().openSession();
        session.beginTransaction();

        Query query = session.createQuery("select t.trackName, r.result, r.date, p.playerName " +
                "from Result as r " +
                "inner join r.track as t " +
                "inner join r.player as p " +
                "order by t.trackName");

        List<Object[]> recordList = query.list();

        for (Object[] result : recordList) {
            System.out.print(String.valueOf(result[0]) + " - ");
            System.out.print(String.valueOf(result[1]) + " - ");
            System.out.print(String.valueOf(result[2]) + " - ");
            System.out.println(String.valueOf(result[3]));
        }

        session.getTransaction().commit();


        ObservableList<Rank> data = FXCollections.observableArrayList();
//        for (Object[] result : database.getAllResults()){
        for (Object[] result : database.getResultsByTrack(1)){
            Rank rank = new Rank(
                    "1",
                    String.valueOf(result[0]),
                    String.valueOf(result[1]),
                    changeDate(String.valueOf(result[2]), "HH:mm '/' dd.MM.yy")
            );
            data.add(rank);
        }


        rankTbl.setItems(data);

    }



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



}
