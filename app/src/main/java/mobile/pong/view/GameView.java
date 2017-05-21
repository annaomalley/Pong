package mobile.pong.view;

import android.view.View;
import android.content.Context;
import android.util.AttributeSet;

import mobile.pong.model.GameModel;

/**
 * Created by Anna on 5/21/17.
 */

public class GameView extends View{

    public GameView(Context context, AttributeSet attrs) {
        super(context, attrs);
        GameModel.getInstance().newGame();
    }

    private void drawCups() {
        Boolean[] currentPlayerCups;
        Boolean[] opponentCups;

        if(GameModel.getInstance().getCurrentPlayer() == 1) {
            opponentCups = GameModel.getInstance().getCups(2);
            currentPlayerCups = GameModel.getInstance().getCups(1);
        }
        else if(GameModel.getInstance().getCurrentPlayer() == 2) {
            opponentCups = GameModel.getInstance().getCups(1);
            currentPlayerCups = GameModel.getInstance().getCups(2);
        }


    }





}
