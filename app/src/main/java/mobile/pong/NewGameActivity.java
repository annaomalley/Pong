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


    static final int USER_LOGIN_REQUEST = 10;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_game_2);

        Button buttonOne = (Button) findViewById(R.id.btnOnePlayer);
        buttonOne.setOnClickListener(new Button.OnClickListener() {
            public void onClick(View v) {
                login(1);
            }
        });

        Button btnTwo = (Button) findViewById(R.id.btnTwoPlayer);
        btnTwo.setOnClickListener(new Button.OnClickListener(){
            public void onClick(View v){
                login(2);
            }
        });

    }

    private void login(int numPlayers){
        Intent intent = new Intent(this, LoginActivity.class);
        startActivityForResult(intent, USER_LOGIN_REQUEST);

    }



    private void returnValues() {
        Intent resultIntent = new Intent();
        resultIntent.putExtra("team1player1", ((TextView) findViewById(R.id.team1player1)).getText().toString());
        resultIntent.putExtra("team1player2", ((TextView) findViewById(R.id.team1player2)).getText().toString());
        resultIntent.putExtra("team2player1", ((TextView) findViewById(R.id.team2player1)).getText().toString());
        resultIntent.putExtra("team2player2", ((TextView) findViewById(R.id.team2player2)).getText().toString());
        setResult(Activity.RESULT_OK, resultIntent);
        finish();
    }

}
