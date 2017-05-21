package mobile.pong.view;

import android.view.View;
import android.content.Context;
import android.util.AttributeSet;
import android.graphics.Paint;
import android.graphics.Color;
import android.graphics.Canvas;
import android.util.Log;
import android.view.MotionEvent;
import android.graphics.Path;
import android.widget.ImageView;
import mobile.pong.R;
import android.graphics.Rect;

import mobile.pong.Player;
import mobile.pong.model.GameModel;

/**
 * Created by Anna on 5/21/17.
 */

public class GameView extends View{

    private Paint paintCups;
    private Paint paintStars;
    private Paint paintX;
    private Paint paintText;

    private int w;
    private int h;

    MotionEvent Xevent;

    public GameView(Context context, AttributeSet attrs) {
        super(context, attrs);

        paintCups = new Paint();
        paintCups.setColor(Color.RED);
        paintCups.setStyle(Paint.Style.STROKE);
        paintCups.setStrokeWidth(5);

        paintStars = new Paint();
        paintStars.setColor(Color.WHITE);
        paintStars.setAntiAlias(true);
        paintStars.setStyle(Paint.Style.STROKE);

        paintX = new Paint();
        paintX.setColor(Color.YELLOW);
        paintX.setStyle(Paint.Style.STROKE);
        paintX.setStrokeWidth(5);

        paintText = new Paint();
        paintX.setColor(Color.BLACK);


    }

    protected void onDraw(Canvas canvas) {
        drawStar(canvas);
        super.onDraw(canvas);
        if(GameModel.getInstance().getGameStarted()) {
            drawCups(canvas);
            if(Xevent!=null) {
                drawX(canvas,Xevent);
            }
            drawTeamOne(canvas);
            drawTeamTwo(canvas);
        }
    }

    private void drawTeamTwo(Canvas canvas) {
        String teamTwoNames = "";
        Player[] teamTwoPlayers = GameModel.getInstance().getTeamTwoPlayers();
        for (int i = 0; i<teamTwoPlayers.length; i++) {
            teamTwoNames+=teamTwoPlayers[i].getName() + "+ ";
        }
        teamTwoNames = teamTwoNames.substring(0,teamTwoNames.length() - 2);

        final float testTextSize = 48f;

        paintText.setTextSize(testTextSize);
        Rect bounds = new Rect();
        paintText.getTextBounds(teamTwoNames, 0, teamTwoNames.length(), bounds);

        // Calculate the desired size as a proportion of our testTextSize.
        float desiredTextSizeWidth = testTextSize * getWidth() / bounds.width();
        float desiredTextSizeHeight = testTextSize * getHeight() / 6 / bounds.height();

        float desiredTextSize = Math.min(desiredTextSizeHeight,desiredTextSizeWidth);

        // Set the paint for that size.
        paintText.setTextSize(desiredTextSize);

        if(GameModel.getInstance().getCurrentTeam()==2) {
            canvas.drawText(teamTwoNames, 0, 4*getHeight() / 6, paintText);
        }
        else {
            canvas.drawText(teamTwoNames, 0, 3*getHeight() / 6, paintText);
        }

    }

    private void drawTeamOne(Canvas canvas) {
        String teamOneNames = "";
        Player[] teamOnePlayers = GameModel.getInstance().getTeamOnePlayers();
        for (int i = 0; i<teamOnePlayers.length; i++) {
            teamOneNames+=teamOnePlayers[i].getName() + "+ ";
        }
        teamOneNames = teamOneNames.substring(0,teamOneNames.length() - 2);

        final float testTextSize = 48f;

        paintText.setTextSize(testTextSize);
        Rect bounds = new Rect();
        paintText.getTextBounds(teamOneNames, 0, teamOneNames.length(), bounds);

        // Calculate the desired size as a proportion of our testTextSize.
        float desiredTextSizeWidth = testTextSize * getWidth() / bounds.width();
        float desiredTextSizeHeight = testTextSize * getHeight() / 6 / bounds.height();

        float desiredTextSize = Math.min(desiredTextSizeHeight,desiredTextSizeWidth);

        // Set the paint for that size.
        paintText.setTextSize(desiredTextSize);

        if(GameModel.getInstance().getCurrentTeam()==1) {
            canvas.drawText(teamOneNames, 0, 4*getHeight() / 6, paintText);
        }
        else {
            canvas.drawText(teamOneNames, 0, 3*getHeight() / 6, paintText);
        }
    }


    private void drawCups(Canvas canvas) {
        Boolean[] currentPlayerCups;
        Boolean[] opponentCups;

        if(GameModel.getInstance().getCurrentTeam() == 1) {
            opponentCups = GameModel.getInstance().getCups(2);
            currentPlayerCups = GameModel.getInstance().getCups(1);
        }
        else {
            opponentCups = GameModel.getInstance().getCups(1);
            currentPlayerCups = GameModel.getInstance().getCups(2);
        }

        float radius = calculateRadius();

        drawBackRows(canvas,radius,currentPlayerCups,opponentCups);
        drawSecondBackRow(canvas,radius,currentPlayerCups,opponentCups);
        drawSecondFrontRow(canvas,radius,currentPlayerCups,opponentCups);
        drawFrontRow(canvas,radius,currentPlayerCups,opponentCups);
    }

    private void drawBackRows(Canvas canvas, float radius, Boolean[] currentPlayerCups, Boolean[] opponentCups) {
        Log.w("height/2", Double.toString((h/2)));
        Log.w("radius", Double.toString((radius)));
        float diameter = radius*2;

        for(int i = 0; i < 4; i++) {
            if(opponentCups[i]) {
                canvas.drawCircle(getWidth() / 2 - diameter - radius + i * diameter, radius, radius, paintCups);
            }
            if(currentPlayerCups[i]) {
                canvas.drawCircle(getWidth() / 2 - diameter - radius + i * diameter, h-radius, radius, paintCups);
            }
        }
    }

    private void drawSecondBackRow(Canvas canvas, float radius, Boolean[] currentPlayerCups, Boolean[] opponentCups) {
        Log.w("height/2", Double.toString((h/2)));
        Log.w("radius", Double.toString((radius)));
        float diameter = radius*2;

        for(int i = 4; i < 7; i++) {
            if(opponentCups[i]) {
                canvas.drawCircle(getWidth() / 2 - diameter + (i-4) * diameter, radius + diameter, radius, paintCups);
            }
            if(currentPlayerCups[i]) {
                canvas.drawCircle(getWidth() / 2 - diameter + (i-4) * diameter, h-radius-diameter, radius, paintCups);
            }
        }
    }

    private void drawSecondFrontRow(Canvas canvas, float radius, Boolean[] currentPlayerCups, Boolean[] opponentCups) {
        float diameter = radius*2;

        for(int i = 7; i < 9; i++) {
            //
            if(opponentCups[i]) {
                //
                canvas.drawCircle(getWidth() / 2 - radius + (i-7)*diameter,
                        diameter*2 + radius, radius, paintCups);
            }
            if(currentPlayerCups[i]) {
                canvas.drawCircle(getWidth() / 2 - radius + (i-7)*diameter,
                        h-radius-diameter*2, radius, paintCups);
            }
        }
    }

    private void drawFrontRow(Canvas canvas,float radius,Boolean[] currentPlayerCups,Boolean[] opponentCups) {
        float diameter = radius*2;

            if(opponentCups[9]) {
                canvas.drawCircle(getWidth() / 2,
                        diameter * 3 + radius, radius, paintCups);
            }
            if(currentPlayerCups[9]) {
                canvas.drawCircle(getWidth() / 2,
                        h-radius-diameter*3, radius, paintCups);
            }
    }

    private float calculateRadius() {
        float radiusHorizontal = w/4;
        float radiusVertical = h/3/4;
        return Math.min(radiusHorizontal,radiusVertical)/2;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        float radius = calculateRadius();
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            int topRow = (int)(Math.floor(((int)event.getY())/(radius*2)));
            Log.v("topRow",Integer.toString(topRow));
            int cell = -1;
            switch (topRow) {
                case 0:
                    cell = handleTouchEventBackRow(event,radius);
                    break;
                case 1:
                    cell = handleTouchEventSecondBackRow(event,radius);
                    break;
                case 2:
                    cell = handleTouchEventSecondFrontRow(event,radius);
                    break;
                case 3:
                    cell = handleTouchEventFrontRow(event,radius);
                    break;
            }
            Log.v("cell",Integer.toString(cell));
            if(cell!=-1) {
                GameModel.getInstance().shot(cell);
                Xevent = event;
                invalidate();
            }
            else {
                Xevent = null;
                invalidate();
            }
        }

        return true;
    }

    public int handleTouchEventBackRow(MotionEvent event,float radius) {
        int column = (int)(Math.floor(((int)event.getX()-getWidth()/2)/(radius*2)+2));
        Log.v("column",Integer.toString(column));
        if(column<4 && column>-1) {
            return column;
        }
        return -1;
    }

    public int handleTouchEventSecondBackRow(MotionEvent event,float radius) {
        int column = (int)(Math.floor(((int)event.getX()-getWidth()/2-radius)/(radius*2)+6));
        Log.v("column",Integer.toString(column));
        if(column<7 && column>-1) {
            return column;
        }
        return -1;
    }

    public int handleTouchEventSecondFrontRow(MotionEvent event,float radius) {
        int column = (int)(Math.floor(((int)event.getX()-getWidth()/2)/(radius*2)+8));
        Log.v("column",Integer.toString(column));
        if(column==7 || column==8) {
            return column;
        }
        return -1;
    }

    public int handleTouchEventFrontRow(MotionEvent event,float radius) {
        int column = (int)(Math.floor(((int)event.getX()-getWidth()/2-radius)/(radius*2)+10));
        Log.v("column",Integer.toString(column));
        if(column==9) {
            return column;
        }
        return -1;
    }







    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        this.w = w;
        this.h = h;
        super.onSizeChanged(w, h, oldw, oldh);
    }

    private void drawStar(Canvas canvas) {
        float mid = getWidth() / 2;
        float min = Math.min(getWidth(), getHeight());
        float fat = min / 17;
        float half = min / 2;
        float rad = half - fat;
        mid = mid - half;
        float diff = half - getHeight()/2;
        Path path = new Path();

        paintStars.setStrokeWidth(fat);
        paintStars.setStyle(Paint.Style.STROKE);

        canvas.drawCircle(mid + half, half - diff, rad, paintStars);

        path.reset();

        paintStars.setStyle(Paint.Style.FILL);


        // top left
        path.moveTo(mid + half * 0.5f, half * 0.84f - diff);
        // top right
        path.lineTo(mid + half * 1.5f, half * 0.84f - diff);
        // bottom left
        path.lineTo(mid + half * 0.68f, half * 1.45f - diff);
        // top tip
        path.lineTo(mid + half * 1.0f, half * 0.5f - diff);
        // bottom right
        path.lineTo(mid + half * 1.32f, half * 1.45f - diff);
        // top left
        path.lineTo(mid + half * 0.5f, half * 0.84f - diff);

        path.close();
        canvas.drawPath(path, paintStars);

    }

    private void drawX(Canvas canvas, MotionEvent event) {
        View view = (View)findViewById(R.id.gameView);

        float sideLength = calculateRadius()/4;
        Log.v("Yvalue:",Float.toString(event.getY()));

        //Log.v("imageView:",Float.toString(imV.getY()));

        float viewY = view.getY();

        canvas.drawLine(event.getX()-sideLength,event.getY()-sideLength - viewY,
                event.getX()+sideLength,event.getY()+sideLength - viewY,paintX);
        canvas.drawLine(event.getX()-sideLength,event.getY()+sideLength - viewY,
                event.getX()+sideLength,event.getY()-sideLength - viewY,paintX);
    }






}
