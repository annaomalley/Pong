package mobile.pong;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.content.Intent;

/**
 * Created by Anna on 5/21/17.
 */

public class NextTurnActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_next_turn);

        Intent intent = getIntent();
        String player = intent.getStringExtra("player");


        TextView tvPlayersTurn = (TextView) findViewById(R.id.whose_turn_textview);
        tvPlayersTurn.setText(player + "'s Turn");

        Button buttonOne = (Button) findViewById(R.id.continue_button);
        buttonOne.setOnClickListener(new Button.OnClickListener() {
            public void onClick(View v) {
                finish();
            }
        });

    }
}
