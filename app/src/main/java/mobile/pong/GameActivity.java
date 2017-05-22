package mobile.pong;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.content.Intent;
import android.app.Activity;
import android.widget.Toast;
import android.widget.Button;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

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
                gameView.invalidate();
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
            Intent intent = new Intent(this, LoginActivity.class);
            startActivityForResult(intent,NEW_GAME_REQUEST);
            return true;
        }
        if (id == R.id.action_leaderboard) {
            Intent intent = new Intent(this, RankingActivity.class);
            startActivity(intent);
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
                    String team1player1name = data.getStringExtra("team1player1");
                    String team1player2name = data.getStringExtra("team1player2");
                    String team2player1name = data.getStringExtra("team2player1");
                    String team2player2name = data.getStringExtra("team2player2");
                    getPlayerFromDB(team1player1name, team1player2name, team2player1name,
                            team2player2name);

                }
                break;
            }
        }
    }


    private void getPlayerFromDB(final String name1, final String name2,
                                 final String name3, final String name4){
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("players");
        ref.addListenerForSingleValueEvent(
                new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        Player player1 = null;
                        Player player2 = null;
                        Player player3 = null;
                        Player player4 = null;
                        for (DataSnapshot messageSnapshot: dataSnapshot.getChildren()) {
                            Player player = messageSnapshot.getValue(Player.class);
                            String name = player.getName();
                            Log.v("nameLALALA", name);
                            if (name != null) {
                                if (name.equals(name1)) {
                                    player1 = player;
                                }
                                if (name.equals(name2)) {
                                    player2 = player;
                                }
                                if (name.equals(name3)) {
                                    player3 = player;
                                }
                                if (name.equals(name4)) {
                                    player4 = player;
                                }
                            }
                        }
                        setPlayer(player1, player2, player3, player4);
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        //handle databaseError
                    }
                });
    }

    private void setPlayer(Player player1, Player player2,
                                        Player player3, Player player4){

        Player[][] teams = new Player[2][2];
        teams[0][0] = player1;
        teams[0][1] = player2;
        teams[1][0] = player3;
        teams[1][1] = player4;


        GameModel.getInstance().newGame(teams[0], teams[1], GameActivity.this);
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
        Toast.makeText(GameActivity.this, msg, Toast.LENGTH_SHORT).show();
    }



}
