package mobile.pong.model;

/**
 * Created by Anna on 5/21/17.
 */

public class GameModel {

    private static GameModel instance = null;
    private Boolean gameStarted = false;

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


    private String[][] players = new String[2][2];

    private int currentPlayer = 1;


    public void newGame(String team1player1, String team1player2, String team2player1, String team2player2) {
        for(int i = 0; i<10; i++) {
            playerOneCups[i] = true;
            playerTwoCups[i] = true;
        }
        currentPlayer = 1;
        this.players[0][0] = team1player1;
        this.players[0][1] = team1player2;
        this.players[1][0] = team2player1;
        this.players[1][1] = team2player2;
        gameStarted = true;
    }

    public void changeCurrentPlayer() {
        currentPlayer = (currentPlayer == 1) ? 2 : 1;
    }

    public int getCurrentPlayer() {
        return currentPlayer;
    }

    public void setField(int player, int cup, boolean value) {
        if(player == 1) {
            playerOneCups[cup] = value;
        }
        else if(player == 2) {
            playerTwoCups[cup] = value;
        }
    }

    public Boolean[] getCups(int player) {
        if (player == 1) {
            return playerOneCups;
        }
        return playerTwoCups;
    }

    public Boolean getGameStarted() {
        return gameStarted;
    }


}
