package mobile.pong;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.support.design.widget.CoordinatorLayout;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import mobile.pong.adapter.PlayerAdapter;
import mobile.pong.data.Player;
import mobile.pong.model.GameModel;
import mobile.pong.utility.GameStarter;

public class RankingActivity extends AppCompatActivity {

    private PlayerAdapter playerAdapter;
    private CoordinatorLayout layoutContent;
    static final int NEW_GAME_REQUEST = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ranking);

        playerAdapter = new PlayerAdapter(this);
        RecyclerView recyclerViewPlayers = (RecyclerView) findViewById(
                R.id.recyclerViewPeople);
        recyclerViewPlayers.setLayoutManager(new LinearLayoutManager(this));
        recyclerViewPlayers.setAdapter(playerAdapter);

        layoutContent = (CoordinatorLayout) findViewById(
                R.id.layoutContent);

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
            startNewGame();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void startNewGame() {
        Intent intent = new Intent(this, LoginActivity.class);
        startActivityForResult(intent, NEW_GAME_REQUEST);
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
