package com.tomczyk.board;

import com.tomczyk.board.livematches.LiveMatches;

public class Main {
    private static final String POLAND = "Poland";
    private static final String GERMANY = "Germany";
    private static final String SPAIN = "Spain";
    private static final String FRANCE = "France";

    public static void main(String[] args) {
        LiveMatches liveMatches = new LiveMatches();
        LiveFootballWorldCupScoreBoard liveFootballWorldCupScoreBoard = new LiveFootballWorldCupScoreBoard(liveMatches);

        liveFootballWorldCupScoreBoard.startGame(POLAND, GERMANY);
        liveFootballWorldCupScoreBoard.startGame(SPAIN, FRANCE);

        liveFootballWorldCupScoreBoard.scoreHome(POLAND, GERMANY);
        liveFootballWorldCupScoreBoard.scoreHome(POLAND, GERMANY);
        liveFootballWorldCupScoreBoard.scoreAway(POLAND, GERMANY);

        liveFootballWorldCupScoreBoard.scoreHome(SPAIN, FRANCE);
        liveFootballWorldCupScoreBoard.scoreAway(SPAIN, FRANCE);

        System.out.println(liveFootballWorldCupScoreBoard.getScoreBoard());

    }
}