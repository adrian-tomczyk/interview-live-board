package com.tomczyk.board;

import com.tomczyk.board.livematches.LiveMatches;
import com.tomczyk.board.match.Match;

public class LiveFootballWorldCupScoreBoard {
    private final LiveMatches liveMatches;

    public LiveFootballWorldCupScoreBoard(LiveMatches liveMatches) {
        this.liveMatches = liveMatches;
    }


    public void startGame(String home, String away) {
        Match match = new Match(home, away);

        liveMatches.addMatch(match);
    }


    public void finishGame(String home, String away) {
        liveMatches.finishMatch(home, away);
    }


    public void scoreHome(String home, String away) {
        liveMatches.scoreHome(home, away);
    }


    public void scoreAway(String home, String away) {
        liveMatches.scoreAway(home, away);
    }


    public String getScoreBoard() {
        return liveMatches.getCurrentMatches().stream()
                .map(Match::getScore)
                .reduce("", (partialString, element) -> partialString + element + "\n")
                .stripTrailing();
    }


}
