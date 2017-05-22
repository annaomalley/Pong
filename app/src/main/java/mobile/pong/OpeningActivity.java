package mobile.pong;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import mobile.pong.data.Player;
import mobile.pong.model.GameModel;
import mobile.pong.utility.GameStarter;

import static mobile.pong.GameActivity.NEW_GAME_REQUEST;


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
        btnLeaderboard.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                openLeaderboard();
            }
        });
    }

    private void startNewGame() {
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
        switch (requestCode) {
            case (NEW_GAME_REQUEST): {
                if (resultCode == Activity.RESULT_OK) {
                    String team1player1name = data.getStringExtra(getString(R.string.team1player1));
                    String team1player2name = data.getStringExtra(getString(R.string.team1player2));
                    String team2player1name = data.getStringExtra(getString(R.string.team2player1));
                    String team2player2name = data.getStringExtra(getString(R.string.team2player2));

                    String[] names = {team1player1name, team1player2name,
                            team2player1name, team2player2name};
                    GameStarter gs = new GameStarter(this, names);
                    gs.start();

                }
                break;
            }
        }
    }

}
