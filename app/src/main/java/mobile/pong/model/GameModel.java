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

    private Boolean[] playerOneCups = new Boolean[10];
    private Boolean[] playerTwoCups = new Boolean[10];

    private int currentPlayer = 1;


    public void newGame() {
        for(int i = 0; i<10; i++) {
            playerOneCups[i] = true;
            playerTwoCups[i] = true;
        }
        currentPlayer = 1;
    }

    public void changeCurrentPlayer() {
        currentPlayer = (currentPlayer == 1) ? 2 : 1;
    }

    public int getCurrentPlayer() {
        return currentPlayer;
    }

    public void setField(short player, short cup, boolean value) {
        if(player == 1) {
            playerOneCups[cup] = value;
        }
        else if(player == 2) {
            playerTwoCups[cup] = value;
        }
    }

    public boolean getField(short player, short cup) {
        if(player == 1) {
            return playerOneCups[cup];
        }
        return playerTwoCups[cup];
    }


}
