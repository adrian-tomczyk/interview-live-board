package com.tomczyk.board.match;

import java.util.Objects;

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

    public String getHomeName(){
        return homeName;
    }

    public String getAwayName(){
        return awayName;
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof Match match)) return false;
        return Objects.equals(homeScore, match.homeScore) && Objects.equals(awayScore, match.awayScore) && Objects.equals(homeName, match.homeName) && Objects.equals(awayName, match.awayName);
    }
}
