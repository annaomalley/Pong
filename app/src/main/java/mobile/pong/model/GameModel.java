package mobile.pong.model;

/**
 * Created by Anna on 5/21/17.
 */

public class GameModel {

    private static GameModel instance = null;

    private GameModel() {
    }

    public static GameModel getInstance() {
        if (instance == null) {
            instance = new GameModel();
        }
        return instance;
    }

}
