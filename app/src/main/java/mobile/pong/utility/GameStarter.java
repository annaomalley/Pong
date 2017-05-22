package mobile.pong.utility;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import mobile.pong.GameActivity;
import mobile.pong.R;
import mobile.pong.data.Player;
import mobile.pong.model.GameModel;

public class GameStarter extends AppCompatActivity {

    private Context context;
    private String[] names = new String[4];

    public GameStarter(Context context, String[] names) {
        this.context = context;
        this.names = names;
    }

    public void start() {
        getPlayerFromDB(names[0], names[1], names[2], names[3]);
    }

    private void getPlayerFromDB(final String name1, final String name2,
                                 final String name3, final String name4) {
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child(context.getString(R.string.fb_players));
        ref.addListenerForSingleValueEvent(
                new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        Player player1 = null;
                        Player player2 = null;
                        Player player3 = null;
                        Player player4 = null;
                        for (DataSnapshot messageSnapshot : dataSnapshot.getChildren()) {
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
                           Player player3, Player player4) {

        Player[][] teams = new Player[2][2];
        teams[0][0] = player1;
        teams[0][1] = player2;
        teams[1][0] = player3;
        teams[1][1] = player4;


        GameModel.getInstance().newGame(teams[0], teams[1]);
        startGameActivity();
    }

    private void startGameActivity() {
        if (!(context instanceof GameActivity)) {
            Intent intent = new Intent(context, GameActivity.class);
            context.startActivity(intent);
        }
    }
}
