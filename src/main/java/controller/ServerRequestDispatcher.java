package controller;

import common.Constant;
import common.ServerRequest;
import javafx.application.Platform;
import javafx.scene.paint.Color;
import model.ServerModel;

/**
 * Created by Mateusz on 16.12.2016.
 */
public class ServerRequestDispatcher{

    private ServerModel serverModel;
    private DatabaseController database;
    private TCPServer tcpServer;

    public ServerRequestDispatcher(){
        serverModel = ServerModel.getInstance();
        tcpServer = TCPServer.getInstance();
        database = new DatabaseController();
        serverModel.setConnection(ServerRequest.NO_CONNECTION);
    }

    public void requestDispatch(String oldValue, String newValue){

        switch(oldValue){
            case ServerRequest.CONNECT:
                Platform.runLater(() -> serverModel.setConnection(ServerRequest.CONNECTED_DEVICE + newValue));
//                serverModel.setColor(Color.GREEN);
                break;

            case ServerRequest.DISCONNECT:
                Platform.runLater(() -> serverModel.setConnection(ServerRequest.NO_CONNECTION));
//                tcpServer = TCPServer.getInstance();
//                tcpServer.start();
                break;

            case ServerRequest.CHECK_USERNAME: checkUsernameAvailability();
                break;
        }

    }

    public void connect(){
//        Platform.runLater(() -> serverModel.setConnection(ServerRequest.CONNECTED_DEVICE + newValue));


    }

    private void checkUsernameAvailability(){
//        if (oldValue != null) {
//
//
//            if (oldValue.equals(ServerRequest.CONNECT)) {
//                Platform.runLater(() -> serverModel.setConnection(ServerRequest.CONNECTED_DEVICE + newValue));
//                labelConnection.setTextFill(Color.GREEN);
//            } else if (newValue.equals(ServerRequest.DISCONNECT)) {
//                Platform.runLater(() -> serverModel.setConnection(ServerRequest.NO_CONNECTION));
//                labelConnection.setTextFill(Color.RED);
//                startServer();
//            } else {
//                serverRequestDispatcher.requestDispatch(newValue, newValue);
//            }
//        }

    }

}
