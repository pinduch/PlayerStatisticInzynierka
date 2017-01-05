package model;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.beans.value.ObservableBooleanValue;
import javafx.beans.value.ObservableValue;
import javafx.scene.paint.Color;

import java.util.Observable;

/**
 * Created by Mateusz on 11.12.2016.
 */
public class ServerModel {

    private StringProperty receivedMessage;
    private StringProperty connection;

    private static ServerModel instance = null;

    public static ServerModel getInstance() {
        if (instance == null) {
            instance = new ServerModel();
        }
        return instance;
    }

    public ServerModel(){
        receivedMessage = new SimpleStringProperty();
        connection = new SimpleStringProperty();
    }

    public StringProperty getReceivedMessage() {
        return receivedMessage;
    }

    public void setReceivedMessage(String receivedMessage) {
        this.receivedMessage.setValue(receivedMessage);
    }

    public StringProperty getConnection() {
        return connection;
    }

    public void setConnection(String connection) {
        this.connection.setValue(connection);
    }

}
