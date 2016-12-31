package controller;

import javafx.application.Platform;
import javafx.scene.paint.Color;
import model.ServerModel;

/**
 * Created by Mateusz on 16.12.2016.
 */
public class ServerRequestDispatcher{

    private ServerModel serverModel;

    public ServerRequestDispatcher(){
        serverModel = ServerModel.getInstance();
        serverModel.setConnection("No device connected to server");
    }

    public void requestDispatch(){
        serverModel.getReceivedMessage().addListener((observable, oldValue, newValue) -> {
            if (newValue.equals("text")) {
                Platform.runLater(() -> serverModel.setConnection("Connected"));
            } else {
                Platform.runLater(() -> serverModel.setConnection("No device connected to server"));
            }
        });
    }


}
