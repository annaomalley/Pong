package mobile.pong.view;

import android.view.View;
import android.content.Context;
import android.util.AttributeSet;
import android.graphics.Paint;
import android.graphics.Color;
import android.graphics.Canvas;
import android.util.Log;

import mobile.pong.model.GameModel;

/**
 * Created by Anna on 5/21/17.
 */

public class GameView extends View{

    private Paint paintCups;

    public GameView(Context context, AttributeSet attrs) {
        super(context, attrs);

        paintCups = new Paint();
        paintCups.setColor(Color.RED);
        paintCups.setStyle(Paint.Style.STROKE);
        paintCups.setStrokeWidth(5);
    }

    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        drawCups(canvas);

    }


    private void drawCups(Canvas canvas) {
        Boolean[] currentPlayerCups;
        Boolean[] opponentCups;

        if(GameModel.getInstance().getCurrentPlayer() == 1) {
            opponentCups = GameModel.getInstance().getCups(2);
            currentPlayerCups = GameModel.getInstance().getCups(1);
        }
        else {
            opponentCups = GameModel.getInstance().getCups(1);
            currentPlayerCups = GameModel.getInstance().getCups(2);
        }

        float radiusHorizontal = getWidth()/4;
        float radiusVertical = getHeight()/3/4;
        float radius = Math.min(radiusHorizontal,radiusVertical)/2;
        //canvas.drawCircle(getWidth()/2,getHeight()/2, radius, paintCups);

        drawBackRows(canvas,radius,currentPlayerCups,opponentCups);
    }

    private void drawBackRows(Canvas canvas, float radius, Boolean[] currentPlayerCups, Boolean[] opponentCups) {
        Log.w("height/2", Double.toString((getHeight()/2)));
        Log.w("radius", Double.toString((radius)));
        float diameter = radius*2;


        canvas.drawCircle(getWidth()/2+radius,diameter+radius,radius,paintCups);
    }





}
