package mobile.pong;

import java.util.Comparator;

/**
 * Created by student on 21/05/2017.
 */

public class Player implements Comparable<Player>{

    public static final int SHOT_MISSED = 0;
    public static final int SHOT_MADE = 1;

    private String name;
    private int shotsMade;
    private int totalShots;
    private int streak;

    private int wins;
    private int losses;
    private double shootingPercentage;

    public Player() {

    }

    public Player(String name){
        this.name = name;
        shotsMade = 0;
        totalShots = 0;
        streak = 0;
        wins = 0;
        losses = 0;
        shootingPercentage = 0;
    }

    public void setShootingPercentage(double shootingPercentage) {
        this.shootingPercentage = shootingPercentage;
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
                if(o1.getShootingPercentage()>o2.getShootingPercentage()) {
                    return -1;
                }
                if(o1.getShootingPercentage()<o2.getShootingPercentage()) {
                    return 1;
                }
                return 0;
            }
        };

    }

    }
