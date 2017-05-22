package mobile.pong;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.content.Intent;
import android.app.Activity;
import android.widget.EditText;
import android.widget.Toast;
import android.widget.Button;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import mobile.pong.data.Player;
import mobile.pong.utility.GameStarter;
import mobile.pong.view.GameView;
import mobile.pong.model.GameModel;

public class GameActivity extends AppCompatActivity {


    public static final int NEW_GAME_REQUEST = 0;
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

        setContext();

        gameView = (GameView) findViewById(R.id.gameView);

        Button buttonOne = (Button) findViewById(R.id.miss_button);
        buttonOne.setOnClickListener(new Button.OnClickListener() {
            public void onClick(View v) {
                GameModel.getInstance().shot(-1);
                gameView.invalidate();
            }
        });

    }

    private void setContext() {
        GameModel.getInstance().setContext(GameActivity.this);
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
            startActivityForResult(intent, NEW_GAME_REQUEST);
            return true;
        }
        if (id == R.id.action_leaderboard) {
            Intent intent = new Intent(this, RankingActivity.class);
            startActivity(intent);
            return true;
        }


        return super.onOptionsItemSelected(item);
    }

    public void showRematchDialog(final int winningTeam) {
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(R.string.dialog_rematch_title);
        builder.setMessage(R.string.dialog_rematch_msg);

        builder.setPositiveButton(R.string.dialog_rematch_yes, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (winningTeam == 1) {
                    GameModel.getInstance().newGame(
                            GameModel.getInstance().getTeamOnePlayers(),
                            GameModel.getInstance().getTeamTwoPlayers(),
                            GameActivity.this
                    );
                } else {
                    GameModel.getInstance().newGame(
                            GameModel.getInstance().getTeamTwoPlayers(),
                            GameModel.getInstance().getTeamOnePlayers(),
                            GameActivity.this
                    );
                }
            }
        });

        builder.setNegativeButton(R.string.dialog_rematch_no, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        builder.show();
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


    public void makeToast(int event) {
        String msg = "";
        if (event == HEATING_UP) {
            msg = getString(R.string.toast_heating);
        }
        if (event == ON_FIRE) {
            msg = getString(R.string.toast_fire);
        }
        if (event == BALLS_BACK) {
            msg = getString(R.string.toast_balls_back);
        }
        if (event == GAME_OVER) {
            msg = getString(R.string.toast_game_over);
        }
        Toast.makeText(GameActivity.this, msg, Toast.LENGTH_SHORT).show();
    }

}
