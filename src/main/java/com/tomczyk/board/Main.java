package com.tomczyk.board;

import com.tomczyk.board.livematches.LiveMatches;
import com.tomczyk.board.match.MatchEventType;

public class Main {
    private static final String POLAND = "Poland";
    private static final String GERMANY = "Germany";
    private static final String SPAIN = "Spain";
    private static final String FRANCE = "France";

    public static void main(String[] args) throws Exception {
        LiveMatches liveMatches = new LiveMatches();
        LiveFootballWorldCupScoreBoard liveFootballWorldCupScoreBoard = new LiveFootballWorldCupScoreBoard(liveMatches);

        liveFootballWorldCupScoreBoard.startGame(POLAND, GERMANY);
        liveFootballWorldCupScoreBoard.startGame(SPAIN, FRANCE);

        liveFootballWorldCupScoreBoard.passMatchEvent(POLAND, GERMANY, MatchEventType.HOME_TEAM_SCORES);
        liveFootballWorldCupScoreBoard.passMatchEvent(POLAND, GERMANY, MatchEventType.HOME_TEAM_SCORES);
        liveFootballWorldCupScoreBoard.passMatchEvent(POLAND, GERMANY, MatchEventType.AWAY_TEAM_SCORES);

        liveFootballWorldCupScoreBoard.passMatchEvent(SPAIN, FRANCE, MatchEventType.HOME_TEAM_SCORES);
        liveFootballWorldCupScoreBoard.passMatchEvent(SPAIN, FRANCE, MatchEventType.AWAY_TEAM_SCORES);

        System.out.println(liveFootballWorldCupScoreBoard.getScoreBoard());

    }
}