package mobile.pong;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import mobile.pong.GameActivity;
import mobile.pong.LoginActivity;
import mobile.pong.Player;
import mobile.pong.R;
import mobile.pong.RankingActivity;
import mobile.pong.model.GameModel;
import mobile.pong.view.GameView;

import static mobile.pong.GameActivity.NEW_GAME_REQUEST;

/**
 * Created by student on 22/05/2017.
 */

public class OpeningActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_opening);

        Button btnNewGame = (Button) findViewById(R.id.btnNewGame);
        btnNewGame.setOnClickListener(new Button.OnClickListener() {
            public void onClick(View v) {
                startNewGame();
            }
        });

        Button btnLeaderboard = (Button) findViewById(R.id.btnLeaderboard);
        btnLeaderboard.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                openLeaderboard();
            }
        });
    }

    private void startNewGame(){
        Intent intent = new Intent(this, LoginActivity.class);
        startActivityForResult(intent, NEW_GAME_REQUEST);
    }

    private void openLeaderboard() {
        Intent intent = new Intent(this, RankingActivity.class);
        startActivity(intent);
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


        GameModel.getInstance().newGame(teams[0], teams[1]);
        startGameActivity();
    }

    private void startGameActivity(){
        Intent intent = new Intent(this, GameActivity.class);
        startActivity(intent);
    }
}
