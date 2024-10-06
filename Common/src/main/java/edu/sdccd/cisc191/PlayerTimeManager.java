package edu.sdccd.cisc191;
import java.text.DecimalFormat;

public class PlayerTimeManager {

    private double playTime; // To store the time in seconds
    private long lastUpdateTime; // To store the last time update was called
    private DecimalFormat dFormat;

    public PlayerTimeManager() {
        this.playTime = 0;
        this.dFormat = new DecimalFormat("0.00");
        this.lastUpdateTime = System.currentTimeMillis(); // Initialize lastUpdateTime with current time
    }

    // Call this method to update the playtime. It should be called in the update loop
    public void updateTime() {
        long currentTime = System.currentTimeMillis();
        long deltaTime = currentTime - lastUpdateTime;
        playTime += (double) deltaTime / 1000.0; // Convert milliseconds to seconds
        lastUpdateTime = currentTime;
    }

    // Call this method to get the current playtime in formatted string
    public String getFormattedPlayTime() {
        return dFormat.format(playTime) + " seconds";
    }

    // Call this method to get the raw playtime in seconds
    public double getPlayTime() {
        return playTime;
    }

    // Optional: reset playtime when starting a new game or resetting level
    public void resetTime() {
        playTime = 0;
        lastUpdateTime = System.currentTimeMillis();
    }
}