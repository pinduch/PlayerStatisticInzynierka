package model;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

/**
 * Created by Mateusz on 11.12.2016.
 */
public class ServerModel {

    public enum DeviceConnection {CONNECTED, DISCONNECTED}
    public enum GameState {START, STOP}

    private GameState gameState;
    private DeviceConnection deviceConnection;

    private StringProperty receivedMessage;

    private static ServerModel instance = null;

    public static ServerModel getInstance() {
        if (instance == null) {
            instance = new ServerModel();
        }
        return instance;
    }

    public ServerModel(){
        receivedMessage = new SimpleStringProperty();
    }

    public GameState getGameState() {
        return gameState;
    }

    public void setGameState(GameState gameState) {
        this.gameState = gameState;
    }

    public DeviceConnection getDeviceConnection() {
        return deviceConnection;
    }

    public void setDeviceConnection(DeviceConnection deviceConnection) {
        this.deviceConnection = deviceConnection;
    }

    public StringProperty getReceivedMessage() {
        return receivedMessage;
    }

    public void setReceivedMessage(String receivedMessage) {
        this.receivedMessage.setValue(receivedMessage);
    }

}
