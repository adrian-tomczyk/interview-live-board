package com.tomczyk.board;

import java.util.ArrayList;
import java.util.List;

public class LiveMatches {
    final ArrayList<Match> matches;

    private boolean areTeamsEqual(Match match, String home, String away) {
        return match.getHomeName().equals(home) && match.getAwayName().equals(away);
    }

    public LiveMatches() {
        matches = new ArrayList<>();
    }

    public List<Match> getCurrentMatches() {
        return matches.stream().toList();
    }

    public void addMatch(Match match) {
        matches.addLast(match);
    }

    public Match getMatch(String home, String away) {
        return matches.stream()
                .filter(match -> areTeamsEqual(match, home, away))
                .findFirst()
                .orElse(null);
    }

    public void finishMatch(String home, String away) throws Exception {
        Match match = getMatch(home, away);

        if (match == null) {
            throw new Exception("Match does not exists");
        }

        matches.remove(match);
    }
}
