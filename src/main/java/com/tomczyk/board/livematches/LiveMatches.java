package com.tomczyk.board.livematches;

import com.tomczyk.board.match.Match;
import com.tomczyk.board.match.MatchEvent;
import com.tomczyk.board.match.exceptions.MatchAlreadyExistsException;
import com.tomczyk.board.match.exceptions.MatchDoesNotExistException;

import java.util.ArrayList;
import java.util.List;

public class LiveMatches {
    private final ArrayList<MatchWrapper> matches;
    private Integer matchesAddedCounter;


    public LiveMatches() {
        matches = new ArrayList<>();
        matchesAddedCounter = 0;
    }


    public void handleMatchEvent(MatchEvent matchEvent) {
        switch (matchEvent.matchEventType()) {
            case MATCH_STARTED -> addMatch(matchEvent);
            case MATCH_FINISHED -> finishMatch(matchEvent);
            default -> passMatchEvent(matchEvent);
        }
    }


    public List<Match> getCurrentMatches() {
        return matches.stream().map(MatchWrapper::match).toList();
    }


    public Match getMatch(String home, String away) {
        MatchWrapper matchWrapper = getOrderedMatchAdapter(home, away);
        if (matchWrapper == null) return null;

        return getOrderedMatchAdapter(home, away).match();
    }


    private void passMatchEvent(MatchEvent matchEvent) {
        MatchWrapper matchWrapper = getOrderedMatchAdapter(matchEvent.home(), matchEvent.away());

        throwIfMatchDoesNotExist(matchWrapper);

        matchWrapper.match().handleMatchEvent(matchEvent);

        updateMatchPosition(matchWrapper);
    }


    private void addMatch(MatchEvent matchEvent) {
        MatchWrapper existingMatch = getOrderedMatchAdapter(matchEvent.home(), matchEvent.away());

        throwIfMatchAlreadyExists(existingMatch);

        Match match = new Match(matchEvent.home(), matchEvent.away());
        MatchWrapper matchWrapper = new MatchWrapper(match, matchesAddedCounter);

        matches.addLast(matchWrapper);
        matchesAddedCounter++;
    }


    private void finishMatch(MatchEvent matchEvent) {
        MatchWrapper matchWrapper = getOrderedMatchAdapter(matchEvent.home(), matchEvent.away());

        throwIfMatchDoesNotExist(matchWrapper);

        matches.remove(matchWrapper);
    }


    private boolean areTeamsEqual(MatchWrapper matchWrapper, String home, String away) {
        return matchWrapper.match().getHomeName().equals(home) && matchWrapper.match().getAwayName().equals(away);
    }


    private MatchWrapper getOrderedMatchAdapter(String home, String away) {
        return matches.stream()
                .filter(match -> areTeamsEqual(match, home, away))
                .findFirst()
                .orElse(null);
    }


    private void updateMatchPosition(MatchWrapper matchWrapper) {
        int insertionIndex = findInsertionIndexAfterUpdate(matchWrapper);

        matches.remove(matchWrapper);
        matches.add(insertionIndex, matchWrapper);
    }


    private int findInsertionIndexAfterUpdate(MatchWrapper matchWrapper) {
        int currentIndex = matches.indexOf(matchWrapper);
        if (currentIndex == 0) return 0;

        for (int i = currentIndex - 1; i >= 0; --i) {
            MatchWrapper matchAtIndex = matches.get(i);

            if (shouldMatchBeInsertedAtPosition(matchWrapper, matchAtIndex)) return i;
        }

        return currentIndex;
    }


    private static boolean shouldMatchBeInsertedAtPosition(MatchWrapper matchWrapper, MatchWrapper matchAtIndex) {
        return isScoreSumHigher(matchWrapper, matchAtIndex) ||
                (isScoreSumEqual(matchWrapper, matchAtIndex) && wasCreatedLater(matchWrapper, matchAtIndex));
    }


    private static boolean wasCreatedLater(MatchWrapper matchWrapper, MatchWrapper matchAtIndex) {
        return matchAtIndex.order() < matchWrapper.order();
    }


    private static boolean isScoreSumEqual(MatchWrapper matchWrapper, MatchWrapper matchAtIndex) {
        return getOrderedMatchAdapterScoreSum(matchAtIndex) == getOrderedMatchAdapterScoreSum(matchWrapper);
    }


    private static boolean isScoreSumHigher(MatchWrapper matchWrapper, MatchWrapper matchAtIndex) {
        return getOrderedMatchAdapterScoreSum(matchAtIndex) < getOrderedMatchAdapterScoreSum(matchWrapper);
    }


    private static int getOrderedMatchAdapterScoreSum(MatchWrapper matchWrapper) {
        return matchWrapper.match().getHomeScore() + matchWrapper.match().getAwayScore();
    }


    private static void throwIfMatchDoesNotExist(MatchWrapper matchWrapper) {
        if (matchWrapper == null) {
            throw new MatchDoesNotExistException();
        }
    }


    private static void throwIfMatchAlreadyExists(MatchWrapper existingMatch) {
        if (existingMatch != null) {
            throw new MatchAlreadyExistsException();
        }
    }
}


