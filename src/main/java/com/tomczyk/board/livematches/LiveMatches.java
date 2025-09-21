package com.tomczyk.board.livematches;

import com.tomczyk.board.match.Match;

import java.util.ArrayList;
import java.util.List;

public class LiveMatches {
    private final ArrayList<Match> matches;


    public LiveMatches() {
        matches = new ArrayList<>();
    }


    public Match getMatchByTeamNames(String home, String away) {
        return matches.stream()
                .filter(match -> MatchComparators.areTeamsEqual(match, home, away))
                .findFirst()
                .orElse(null);
    }


    public void addMatch(Match match) {
        Match existingMatch = getMatchByTeamNames(match.getHomeName(), match.getAwayName());

        MatchListVerifications.throwIfMatchAlreadyExists(existingMatch);

        matches.addLast(match);
    }


    public void finishMatch(String home, String away) {
        Match match = getMatchByTeamNames(home, away);

        MatchListVerifications.throwIfMatchDoesNotExist(match);

        matches.remove(match);
    }


    public void updateScore(String home, String away, Integer scoreHome, Integer scoreAway) {
        Match match = getMatchByTeamNames(home, away);

        MatchListVerifications.throwIfMatchDoesNotExist(match);

        match.updateScore(scoreHome, scoreAway);

        sortMatches();
    }


    public List<Match> getCurrentMatches() {
        return matches;
    }


    private void sortMatches() {
        matches.sort((match1, match2) -> {
            if (MatchComparators.isScoreSumLower(match1, match2)) return 1;
            if (MatchComparators.isScoreSumEqual(match1, match2) && MatchComparators.wasCreatedBefore(match1, match2))
                return 1;

            return -1;
        });
    }

}


