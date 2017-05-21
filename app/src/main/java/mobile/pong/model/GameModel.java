package mobile.pong.model;

import android.content.Context;
import android.widget.Toast;

import mobile.pong.GameActivity;
import mobile.pong.Player;

import static java.security.AccessController.getContext;

/**
 * Created by Anna on 5/21/17.
 */

public class GameModel {

    private static GameModel instance = null;
    private boolean gameStarted = false;

    private GameModel() {
    }

    public static GameModel getInstance() {
        if (instance == null) {
            instance = new GameModel();
        }
        return instance;
    }

    private Boolean[] teamOneCups = new Boolean[10];
    private Boolean[] teamTwoCups = new Boolean[10];

    private boolean playerOneMake = false;
    private boolean ballsBack = false;

    private Player[] teamOne;
    private Player[] teamTwo;

    private int currentTeam = 1;
    private int currentPlayer = 1;

    private boolean gameOver = false;

    private Context context;


    public void newGame(Player[] teamOne, Player[] teamTwo, Context context) {
        if (teamOne.length != 2 || teamTwo.length != 2) {
            throw new IllegalStateException("team size must be 2");
        }

        for (int i = 0; i < 10; i++) {
            teamOneCups[i] = true;
            teamTwoCups[i] = true;
        }

        currentPlayer = 1;
        currentTeam = 1;

        this.teamOne = teamOne;
        this.teamTwo = teamTwo;

        playerOneMake = false;
        ballsBack = false;
        gameOver = false;

        this.context = context;

        gameStarted = true;
    }


    public void changeCurrentTeam() {
        ballsBack = false;
        currentTeam = (currentTeam == 1) ? 2 : 1;
    }

    public void changeCurrentPlayer() {
        if (currentPlayer == 1) {
            currentPlayer = 2;
        } else {
            playerOneMake = false;
            currentPlayer = 1;
        }
    }

    public int getCurrentTeam() {
        return currentTeam;
    }

    public int getCurrentPlayer() {
        return currentPlayer;
    }

    private Player getPlayerObj() {
        if (getCurrentTeam() == 1) {
            return teamOne[getCurrentPlayer() - 1];
        }
        return teamTwo[getCurrentPlayer() - 1];
    }

    /* Cup is the cup number if a cup is made, or -1 if the shot was a miss. */
    public void shot(int cup) {
        if (!gameOver()) {
            Player shooter = getPlayerObj();

            if (cup >= 0) {
                shooter.shoot(Player.SHOT_MADE);
                if (getCurrentTeam() == 1) {
                    teamOneCups[cup] = false;
                } else {
                    teamTwoCups[cup] = false;
                }
                if (getCurrentPlayer() == 1) {
                    playerOneMake = true;
                }
            } else {
                shooter.shoot(Player.SHOT_MISSED);
            }

            if (gameOver()) {
                gameOver = true;
                ((GameActivity) context).makeToast(GameActivity.GAME_OVER);
            } else {
                nextTurn();
            }
        }
    }

    private boolean gameOver() {
        if (countCups(teamOneCups) == 0){
            setWin(teamTwo);
            setLoss(teamOne);
            return true;
        }
        if (countCups(teamTwoCups) == 0){
            setWin(teamOne);
            setLoss(teamTwo);
            return true;
        }
        return false;
    }

    private void setWin(Player[] team){
        Player p0 = team[0];
        Player p1 = team[1];
        if (p0.equals(p1)){
            p0.win();
        }
        else{
            p0.win();
            p1.win();
        }
    }

    private void setLoss(Player[] team){
        Player p0 = team[0];
        Player p1 = team[1];
        if (p0.equals(p1)){
            p0.lose();
        }
        else{
            p0.lose();
            p1.lose();
        }
    }


    private int countCups(Boolean[] cupList) {
        int cups = 0;
        for (int i = 0; i < cupList.length; i++) {
            if (cupList[i] == true) {
                cups++;
            }
        }
        return cups;
    }

    private void nextTurn() {
        int playerNumber = getCurrentPlayer();
        Player shooter = getPlayerObj();
        int streak = shooter.getStreak();

        if (playerNumber == 1) {
            if (streak == 0) {
                changeCurrentPlayer();
                return;
            }
            if (streak == 1) {
                playerOneMake = true;
                changeCurrentPlayer();
                return;
            }
            if (streak == 2) {
                playerOneMake = true;
                ((GameActivity) context).makeToast(GameActivity.HEATING_UP);
                changeCurrentPlayer();
                return;
            }
            if (streak >= 3) {
                ((GameActivity) context).makeToast(GameActivity.ON_FIRE);
                return;
            }
        }

        /* Player 2 */
        else {
            if (streak == 0) {
                if (ballsBack) {
                    ((GameActivity) context).makeToast(GameActivity.BALLS_BACK);
                    ballsBack = false;
                    changeCurrentPlayer();
                    return;
                } else {
                    changeCurrentPlayer();
                    changeCurrentTeam();
                    return;
                }
            }
            if (streak == 1) {
                if (playerOneMake) {
                    changeCurrentPlayer();
                    return;
                } else {
                    changeCurrentPlayer();
                    changeCurrentTeam();
                    return;
                }
            }
            if (streak == 2) {
                ((GameActivity) context).makeToast(GameActivity.HEATING_UP);
                if (playerOneMake) {
                    changeCurrentPlayer();
                    return;
                } else {
                    changeCurrentPlayer();
                    changeCurrentTeam();
                    return;
                }
            }
            if (streak >= 3) {
                ((GameActivity) context).makeToast(GameActivity.ON_FIRE);
                if (playerOneMake) {
                    ballsBack = true;
                }
                return;
            }
        }
    }

    public Boolean[] getCups(int team) {
        if (team == 1) {
            return teamOneCups;
        }
        return teamTwoCups;
    }

    public Boolean getGameStarted() {
        return gameStarted;
    }


}
