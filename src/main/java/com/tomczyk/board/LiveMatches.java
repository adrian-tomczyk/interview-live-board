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

    private static void throwUnexpectedEventType(List<MatchEventType> expectedMatchEventType, MatchEvent matchEvent) throws Exception {
        if(!expectedMatchEventType.contains(matchEvent.matchEventType())){
            throw new Exception("MatchEventType mismatch - expected: " + expectedMatchEventType.stream().map(Enum::toString).toList() + " got: "+ matchEvent.matchEventType().toString());
        }
    }

    public LiveMatches() {
        matches = new ArrayList<>();
    }

    public List<Match> getCurrentMatches() {
        return matches.stream().toList();
    }

    public void addMatch(MatchEvent matchEvent) throws Exception {
        throwUnexpectedEventType(List.of(MatchEventType.MATCH_STARTED), matchEvent);

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
        throwUnexpectedEventType(List.of(MatchEventType.MATCH_FINISHED), matchEvent);

        Match match = getMatch(matchEvent.home(), matchEvent.away());

        if (match == null) {
            throw new Exception("Match does not exists");
        }

        matches.remove(match);
    }

    public void updateMatchScore(MatchEvent matchEvent) throws Exception {
        throwUnexpectedEventType(List.of(MatchEventType.HOME_TEAM_SCORES, MatchEventType.AWAY_TEAM_SCORES), matchEvent);

        Match match = getMatch(matchEvent.home(), matchEvent.away());

        switch (matchEvent.matchEventType()){
            case HOME_TEAM_SCORES -> match.scoreHome();
            case AWAY_TEAM_SCORES -> match.scoreAway();
        }
    }
}
