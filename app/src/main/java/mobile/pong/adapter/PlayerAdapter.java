package mobile.pong.adapter;

import android.support.v7.widget.RecyclerView;
import android.widget.TextView;
import android.view.View;
import java.util.List;
import android.content.Context;
import android.view.ViewGroup;
import android.view.LayoutInflater;
import java.util.Collections;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import java.util.Map;
import java.util.LinkedList;
import android.util.Log;
import java.util.concurrent.TimeUnit;


import mobile.pong.R;
import mobile.pong.Player;




/**
 * Created by Anna on 5/22/17.
 */

public class PlayerAdapter extends RecyclerView.Adapter<PlayerAdapter.ViewHolder> {

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView tvName;
        public TextView tvPercentage;

        public ViewHolder(View itemView) {
            super(itemView);
            tvName = (TextView) itemView.findViewById(R.id.tvName);
            tvPercentage = (TextView) itemView.findViewById(R.id.tvPercentage);
        }
    }

    private List<Player> playerList;
    private Context context;
    private int lastPosition = -1;

    public PlayerAdapter(Context context) {
        playerList = getUsers();
        Log.v("size",Integer.toString(playerList.size()));
        //sortplayerList();
        this.context = context;
    }

    private List<Player> getUsers() {
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("players");
        final List<Player> playerList = new LinkedList<Player>();
        ref.addListenerForSingleValueEvent(
                new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        for (DataSnapshot messageSnapshot: dataSnapshot.getChildren()) {
                            Player player = messageSnapshot.getValue(Player.class);
                            addPlayer(player);
                            Log.v("ADDED",player.getName());

                        }
                    }
                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        //handle databaseError
                    }
                });
        return playerList;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.row_player, viewGroup, false);
        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(final ViewHolder viewHolder, final int position) {
        viewHolder.tvName.setText(playerList.get(position).getName());
        viewHolder.tvPercentage.setText(Double.toString(playerList.get(position).getShootingPercentage()));
        setAnimation(viewHolder.itemView, position);
    }

    @Override
    public int getItemCount() {
        return playerList.size();
    }

    public void addPlayer(Player player) {
        playerList.add(player);
        playerList.sort(Player.Comparators.PERCENTAGE);
        notifyDataSetChanged();
    }

    public void updatePlayer(int index, Player player) {
        playerList.set(index, player);
        notifyItemChanged(index);

    }

    public void removePlace(int index) {
        playerList.remove(index);
        notifyItemRemoved(index);
    }

    public void swapPlaces(int oldPosition, int newPosition) {
        if (oldPosition < newPosition) {
            for (int i = oldPosition; i < newPosition; i++) {
                Collections.swap(playerList, i, i + 1);
            }
        } else {
            for (int i = oldPosition; i > newPosition; i--) {
                Collections.swap(playerList, i, i - 1);
            }
        }
        notifyItemMoved(oldPosition, newPosition);
    }

    public Player getPlayer(int i) {
        return playerList.get(i);
    }

    private void setAnimation(View viewToAnimate, int position) {
        // If the bound view wasn't previously displayed on screen, it's animated
        if (position > lastPosition) {
            Animation animation = AnimationUtils.loadAnimation(context, android.R.anim.slide_in_left);
            viewToAnimate.startAnimation(animation);
            lastPosition = position;
        }
    }

}
