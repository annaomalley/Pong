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





}
