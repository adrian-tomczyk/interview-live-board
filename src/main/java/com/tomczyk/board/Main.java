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

        liveFootballWorldCupScoreBoard.updateScore(POLAND, GERMANY, 2, 1);

        liveFootballWorldCupScoreBoard.updateScore(SPAIN, FRANCE, 0, 2);

        System.out.println(liveFootballWorldCupScoreBoard.getScoreBoard());

    }
}