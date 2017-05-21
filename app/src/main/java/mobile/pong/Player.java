package mobile.pong;

/**
 * Created by student on 21/05/2017.
 */

public class Player {

    public static final int SHOT_MISSED = 0;
    public static final int SHOT_MADE = 1;

    private String name;
    private int shotsMade;
    private int totalShots;
    private int streak;

    private int wins;
    private int losses;

    public Player(String name){
        this.name = name;
        shotsMade = 0;
        totalShots = 0;
        streak = 0;
        wins = 0;
        losses = 0;
    }


    public void shoot(int result){
        totalShots++;
        if (result == SHOT_MADE){
            shotsMade ++;
            streak++;
        }
        else{
            streak = 0;
        }
    }

    public String getName() {
        return name;
    }

    public int getShotsMade(){ return shotsMade; }

    public int getTotalShots(){ return totalShots; }

    public double getShootingPercentage() {
        if (totalShots == 0) {
            return 0;
        } else {
            return (1.0 * shotsMade) / totalShots;
        }
    }

    public int getStreak() { return streak; }

    public int getWins(){ return wins;}
    public int getLosses(){ return losses;}

    public void lose(){
        losses++;
    }

    public void win(){
        wins++;
    }

    public boolean equals(Player other){
        return this.name.equals(other.name);
    }
}
