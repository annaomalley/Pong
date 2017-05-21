package mobile.pong;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.widget.Button;
import android.view.View;
import android.content.Intent;
import android.widget.TextView;
import android.app.Activity;

import mobile.pong.view.GameView;

/**
 * Created by Anna on 5/21/17.
 */

public class NewGameActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_game);

        Button buttonOne = (Button) findViewById(R.id.new_game_button);
        buttonOne.setOnClickListener(new Button.OnClickListener() {
            public void onClick(View v) {
                returnValues();
            }
        });

    }

    private void returnValues() {
        Intent resultIntent = new Intent();
        resultIntent.putExtra("team1player1", ((TextView) findViewById(R.id.team1player1)).getText().toString());
        resultIntent.putExtra("team1player2", ((TextView) findViewById(R.id.team1player2)).getText().toString());
        resultIntent.putExtra("team2player1", ((TextView) findViewById(R.id.team2player1)).getText().toString());
        resultIntent.putExtra("team1player2", ((TextView) findViewById(R.id.team2player2)).getText().toString());
        setResult(Activity.RESULT_OK, resultIntent);
        finish();
    }

}
