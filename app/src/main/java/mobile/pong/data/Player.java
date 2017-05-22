package mobile.pong.data;

import java.util.Comparator;
import java.util.Map;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Exclude;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;


public class Player implements Comparable<Player> {

    public static final int SHOT_MISSED = 0;
    public static final int SHOT_MADE = 1;

    private String uId;
    private String name;
    private int shotsMade;
    private int totalShots;
    private int streak;

    private int wins;
    private int losses;
    private double shootingPercentage;
    private double winPercentage;

    public Player() {

    }

    public Player(String name, String key) {
        this.name = name;
        this.uId = key;
        shotsMade = 0;
        shootingPercentage = 0;
        winPercentage = 0;
        totalShots = 0;
        streak = 0;
        wins = 0;
        losses = 0;
    }

    public void setShootingPercentage(double shootingPercentage) {
        this.shootingPercentage = shootingPercentage;
    }


    public void shoot(int result) {
        totalShots++;
        if (result == SHOT_MADE) {
            shotsMade++;
            streak++;
        } else {
            streak = 0;
        }
        updateShotPercentage();
        updateDatabase();
    }

    public String getName() {
        return name;
    }

    public int getShotsMade() {
        return shotsMade;
    }

    public int getTotalShots() {
        return totalShots;
    }

    public double getShootingPercentage() {
        return shootingPercentage;
    }

    public void updateShotPercentage() {
        if (totalShots == 0) {
            shootingPercentage = 0;
        } else {
            shootingPercentage = (100.0 * shotsMade) / totalShots;
        }
    }

    public String getuId() {
        return uId;
    }

    public void setuId(String uId) {
        this.uId = uId;
    }

    public int getStreak() {
        return streak;
    }

    public int getWins() {
        return wins;
    }

    public int getLosses() {
        return losses;
    }

    public double getWinPercentage() {
        return winPercentage;
    }

    public void updateWinPercentage() {
        int games = wins + losses;
        winPercentage = (100.0 * wins) / games;
    }

    public void lose() {
        losses++;
        updateWinPercentage();
        updateDatabase();
    }

    public void win() {
        wins++;
        updateWinPercentage();
        updateDatabase();
    }

    public void resetStreak() {
        streak = 0;
    }

    public boolean equals(Player other) {
        return this.name.equals(other.name);
    }


    @Exclude
    public Map<String, Object> toMap() {
        HashMap<String, Object> result = new HashMap<>();
        result.put("losses", losses);
        result.put("name", name);
        result.put("shootingPercentage", shootingPercentage);
        result.put("shotsMade", shotsMade);
        result.put("streak", streak);
        result.put("totalShots", totalShots);
        result.put("uId", uId);
        result.put("winPercentage", winPercentage);
        result.put("wins", wins);
        return result;
    }

    private void updateDatabase() {
        DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference().child("players").child(getuId());
        mDatabase.updateChildren(toMap());
    }


    @Override
    public int compareTo(Player o) {
        return Comparators.PERCENTAGE.compare(this, o);
    }

    public static class Comparators {

        public static Comparator<Player> NAME = new Comparator<Player>() {
            @Override
            public int compare(Player o1, Player o2) {
                return o1.getName().compareTo(o2.getName());
            }
        };

        public static Comparator<Player> PERCENTAGE = new Comparator<Player>() {
            @Override
            public int compare(Player o1, Player o2) {
                if (o1.getShootingPercentage() > o2.getShootingPercentage()) {
                    return -1;
                }
                if (o1.getShootingPercentage() < o2.getShootingPercentage()) {
                    return 1;
                }
                return 0;
            }
        };

    }

}
