package model;

/**
 * Created by Mateusz on 11.12.2016.
 */
public class ServerModel {

    public enum GameState {START, STOP}

    private GameState gameState;

    private static ServerModel instance = null;

    public static ServerModel getInstance() {
        if (instance == null) {
            instance = new ServerModel();
        }
        return instance;
    }

    public GameState getGameState() {
        return gameState;
    }

    public void setGameState(GameState gameState) {
        this.gameState = gameState;
    }
}
