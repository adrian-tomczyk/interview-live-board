package com.tomczyk.board;

import com.tomczyk.board.livematches.LiveMatches;
import com.tomczyk.board.match.Match;
import com.tomczyk.board.match.MatchEvent;
import com.tomczyk.board.match.MatchEventType;

public class LiveFootballWorldCupScoreBoard {
    private final LiveMatches liveMatches;

    public LiveFootballWorldCupScoreBoard(LiveMatches liveMatches){
        this.liveMatches = liveMatches;
    }


    public void startGame(String home, String away) throws Exception {
        liveMatches.addMatch(new MatchEvent(MatchEventType.MATCH_STARTED, home, away));
    }


    public void finishGame(String home, String away) throws Exception {
        liveMatches.finishMatch(new MatchEvent(MatchEventType.MATCH_FINISHED, home, away));
    }


    public void updateScore(String home, String away, MatchEventType matchEventType) throws Exception {
        liveMatches.updateMatchScore(new MatchEvent(matchEventType, home, away));
    }

    public String getScoreBoard() {
        return liveMatches.getCurrentMatches().stream()
                .map(Match::getScore)
                .reduce("", (partialString, element) -> partialString + element + "\n")
                .stripTrailing();
    }
}
