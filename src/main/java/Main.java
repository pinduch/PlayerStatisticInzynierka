/**
 * Created by Mateusz on 14.11.2016.
 */

import controller.TCPServer;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import utils.HibernateUtil;

import java.io.IOException;

public class Main extends Application {

    private static Stage primaryStage;
    private static AnchorPane mainPane;

    public static void main(String[] args) throws IOException {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws IOException {

        this.primaryStage = primaryStage;
        this.primaryStage.setTitle("Ollie Player Statistics");

        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(Main.class.getResource("test.fxml"));
        mainPane = loader.load();
        Scene scene = new Scene(mainPane);
        primaryStage.setScene(scene);
        primaryStage.show();

        primaryStage.setOnCloseRequest(e -> closeOpenedConnections());

    }

    private void closeOpenedConnections(){
        HibernateUtil.shutdown();
        TCPServer.getInstance().closeConnection();
    }
}
