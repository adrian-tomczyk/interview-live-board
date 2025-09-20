package com.tomczyk.board;

import com.tomczyk.board.match.Match;
import com.tomczyk.board.match.MatchEvent;
import com.tomczyk.board.match.MatchEventType;

import java.util.ArrayList;
import java.util.List;

public class LiveMatches {
    final ArrayList<Match> matches;

    private boolean areTeamsEqual(Match match, String home, String away) {
        return match.getHomeName().equals(home) && match.getAwayName().equals(away);
    }

    private static void throwUnexpectedEventType(MatchEventType expectedMatchEventType, MatchEvent matchEvent) throws Exception {
        if(matchEvent.matchEventType() != expectedMatchEventType){
            throw new Exception("MatchEventType mismatch - expected: " + expectedMatchEventType.toString() + " got: "+ matchEvent.matchEventType().toString());
        }
    }

    public LiveMatches() {
        matches = new ArrayList<>();
    }

    public List<Match> getCurrentMatches() {
        return matches.stream().toList();
    }

    public void addMatch(MatchEvent matchEvent) throws Exception {
        throwUnexpectedEventType(MatchEventType.MATCH_STARTED, matchEvent);

        Match match = new Match(matchEvent.home(), matchEvent.away());
        matches.addLast(match);
    }

    public Match getMatch(String home, String away) {
        return matches.stream()
                .filter(match -> areTeamsEqual(match, home, away))
                .findFirst()
                .orElse(null);
    }

    public void finishMatch(MatchEvent matchEvent) throws Exception {
        throwUnexpectedEventType(MatchEventType.MATCH_FINISHED, matchEvent);


        Match match = getMatch(matchEvent.home(), matchEvent.away());

        if (match == null) {
            throw new Exception("Match does not exists");
        }

        matches.remove(match);
    }
}
