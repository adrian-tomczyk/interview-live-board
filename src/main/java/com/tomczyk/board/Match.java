package com.tomczyk.board;

public class Match {
    private Integer homeScore;
    private Integer awayScore;
    private final String homeName;
    private final String awayName;

    public Match(String home, String away) {
        homeName = home;
        awayName = away;
        homeScore = 0;
        awayScore = 0;
    }

    public String getScore() {
        return String.format("%s - %s: %d-%d", homeName, awayName, homeScore, awayScore);
    }

    public void scoreHome() {
        homeScore++;
    }

    public void scoreAway() {
        awayScore++;
    }
}
