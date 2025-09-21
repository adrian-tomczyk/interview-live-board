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


    public String getHomeName() {
        return homeName;
    }


    public String getAwayName() {
        return awayName;
    }


    public Integer getHomeScore() {
        return homeScore;
    }


    public Integer getAwayScore() {
        return awayScore;
    }


    public void handleMatchEvent(MatchEvent matchEvent) throws Exception {
        throwIfEventTeamsDoesNotEqual(matchEvent);

        switch (matchEvent.matchEventType()) {
            case HOME_TEAM_SCORES -> scoreHome();
            case AWAY_TEAM_SCORES -> scoreAway();
            default -> System.out.println("Unexpected event type: " + matchEvent.matchEventType().name());
        }
    }


    @Override
    public boolean equals(Object o) {
        if (!(o instanceof Match match)) return false;
        return Objects.equals(homeScore, match.homeScore) && Objects.equals(awayScore, match.awayScore) && Objects.equals(homeName, match.homeName) && Objects.equals(awayName, match.awayName);
    }


    private boolean areEventTeamsEqual(MatchEvent matchEvent) {
        return homeName.equals(matchEvent.home()) && awayName.equals(matchEvent.away());
    }


    private void throwIfEventTeamsDoesNotEqual(MatchEvent matchEvent) throws Exception {
        if (!areEventTeamsEqual(matchEvent)) {
            throw new Exception("Match teams do not match");
        }
    }

}
