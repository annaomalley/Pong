package mobile.pong;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.support.design.widget.CoordinatorLayout;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import mobile.pong.adapter.PlayerAdapter;

/**
 * Created by Anna on 5/22/17.
 */

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

        //setUpToolBar();
    }

    private void setUpToolBar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
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


}
