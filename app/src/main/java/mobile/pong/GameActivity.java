package mobile.pong;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.content.Intent;
import android.app.Activity;
import android.widget.Toast;
import android.widget.Button;

import mobile.pong.view.GameView;
import mobile.pong.model.GameModel;

public class GameActivity extends AppCompatActivity {


    static final int NEW_GAME_REQUEST = 0;
    public static final int HEATING_UP = 1;
    public static final int ON_FIRE = 2;
    public static final int BALLS_BACK = 3;
    public static final int GAME_OVER = 4;

    private GameView gameView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        gameView = (GameView) findViewById(R.id.gameView);

        Button buttonOne = (Button) findViewById(R.id.miss_button);
        buttonOne.setOnClickListener(new Button.OnClickListener() {
            public void onClick(View v) {
                GameModel.getInstance().shot(-1);
            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_game, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_new_game) {
            Intent intent = new Intent(this, NewGameActivity.class);
            startActivityForResult(intent,NEW_GAME_REQUEST);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }



    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch(requestCode) {
            case (NEW_GAME_REQUEST) : {
                if (resultCode == Activity.RESULT_OK) {
                    String team1player1 = data.getStringExtra("team1player1");
                    Player player1 = new Player(team1player1);
                    String team1player2 = data.getStringExtra("team1player2");
                    Player player2 = new Player(team1player2);

                    Player[] teamOne = {player1, player2};

                    String team2player1 = data.getStringExtra("team2player1");
                    Player player3 = new Player(team2player1);
                    String team2player2 = data.getStringExtra("team2player2");
                    Player player4 = new Player(team2player2);
                    Player[] teamTwo = {player3, player4};

                    GameModel.getInstance().newGame(teamOne, teamTwo, getApplicationContext());
                }
                break;
            }
        }
    }

    public void makeToast(int event){
        String msg = "";
        if (event == HEATING_UP){
            msg = "Heating up!";
        }
        if (event == ON_FIRE){
            msg = "On fire! Shoot again";
        }
        if (event == BALLS_BACK){
            msg = "Balls back";
        }
        if (event == GAME_OVER){
            msg = "Game over!";
        }
        Toast.makeText(GameActivity.this, msg, Toast.LENGTH_LONG).show();
    }

}
