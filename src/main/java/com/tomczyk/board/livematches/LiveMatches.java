package com.tomczyk.board.livematches;

import com.tomczyk.board.match.Match;
import com.tomczyk.board.match.MatchEvent;

import java.util.ArrayList;
import java.util.List;

public class LiveMatches {
    private final ArrayList<OrderedMatchAdapter> matches;
    private Integer matchesAddedCounter;


    public LiveMatches() {
        matches = new ArrayList<>();
        matchesAddedCounter = 0;
    }


    public void handleMatchEvent(MatchEvent matchEvent) throws Exception {
        passMatchEvent(matchEvent);

        switch (matchEvent.matchEventType()) {
            case MATCH_STARTED -> addMatch(matchEvent);
            case MATCH_FINISHED -> finishMatch(matchEvent);
        }
    }


    public List<Match> getCurrentMatches() {
        return matches.stream().map(OrderedMatchAdapter::match).toList();
    }


    public Match getMatch(String home, String away) {
        OrderedMatchAdapter orderedMatchAdapter = getOrderedMatchAdapter(home, away);
        if (orderedMatchAdapter == null) return null;

        return getOrderedMatchAdapter(home, away).match();
    }


    private void passMatchEvent(MatchEvent matchEvent) throws Exception {
        OrderedMatchAdapter orderedMatchAdapter = getOrderedMatchAdapter(matchEvent.home(), matchEvent.away());

        throwIfMatchDoesNotExists(orderedMatchAdapter);

        orderedMatchAdapter.match().handleMatchEvent(matchEvent);

        updateMatchPosition(orderedMatchAdapter);
    }


    private void addMatch(MatchEvent matchEvent) throws Exception {
        OrderedMatchAdapter existingMatch = getOrderedMatchAdapter(matchEvent.home(), matchEvent.away());

        throwIfMatchAlreadyExists(existingMatch);

        Match match = new Match(matchEvent.home(), matchEvent.away());

        OrderedMatchAdapter orderedMatchAdapter = new OrderedMatchAdapter(match, matchesAddedCounter);

        matches.addLast(orderedMatchAdapter);
        matchesAddedCounter++;
    }


    private void finishMatch(MatchEvent matchEvent) throws Exception {
        OrderedMatchAdapter orderedMatchAdapter = getOrderedMatchAdapter(matchEvent.home(), matchEvent.away());

        throwIfMatchDoesNotExists(orderedMatchAdapter);

        matches.remove(orderedMatchAdapter);
    }


    private boolean areTeamsEqual(OrderedMatchAdapter orderedMatchAdapter, String home, String away) {
        return orderedMatchAdapter.match().getHomeName().equals(home) && orderedMatchAdapter.match().getAwayName().equals(away);
    }


    private OrderedMatchAdapter getOrderedMatchAdapter(String home, String away) {
        return matches.stream()
                .filter(match -> areTeamsEqual(match, home, away))
                .findFirst()
                .orElse(null);
    }


    private void updateMatchPosition(OrderedMatchAdapter orderedMatchAdapter) {
        int insertionIndex = findInsertionIndexAfterUpdate(orderedMatchAdapter);

        matches.remove(orderedMatchAdapter);
        matches.add(insertionIndex, orderedMatchAdapter);
    }


    private int findInsertionIndexAfterUpdate(OrderedMatchAdapter orderedMatchAdapter) {
        int currentIndex = matches.indexOf(orderedMatchAdapter);
        if (currentIndex == 0) return 0;

        for (int i = currentIndex - 1; i >= 0; --i) {
            OrderedMatchAdapter matchAtIndex = matches.get(i);

            if (shouldMatchBeInsertedAtPosition(orderedMatchAdapter, matchAtIndex)) return i;
        }

        return currentIndex;
    }


    private static boolean shouldMatchBeInsertedAtPosition(OrderedMatchAdapter orderedMatchAdapter, OrderedMatchAdapter matchAtIndex) {
        return isScoreSumHigher(orderedMatchAdapter, matchAtIndex) ||
                (isScoreSumEqual(orderedMatchAdapter, matchAtIndex) && wasCreatedLater(orderedMatchAdapter, matchAtIndex));
    }


    private static boolean wasCreatedLater(OrderedMatchAdapter orderedMatchAdapter, OrderedMatchAdapter matchAtIndex) {
        return matchAtIndex.order() < orderedMatchAdapter.order();
    }


    private static boolean isScoreSumEqual(OrderedMatchAdapter orderedMatchAdapter, OrderedMatchAdapter matchAtIndex) {
        return getOrderedMatchAdapterScoreSum(matchAtIndex) == getOrderedMatchAdapterScoreSum(orderedMatchAdapter);
    }


    private static boolean isScoreSumHigher(OrderedMatchAdapter orderedMatchAdapter, OrderedMatchAdapter matchAtIndex) {
        return getOrderedMatchAdapterScoreSum(matchAtIndex) < getOrderedMatchAdapterScoreSum(orderedMatchAdapter);
    }


    private static int getOrderedMatchAdapterScoreSum(OrderedMatchAdapter orderedMatchAdapter) {
        return orderedMatchAdapter.match().getHomeScore() + orderedMatchAdapter.match().getAwayScore();
    }


    private static void throwIfMatchDoesNotExists(OrderedMatchAdapter orderedMatchAdapter) throws Exception {
        if (orderedMatchAdapter == null) {
            throw new Exception("Match does not exists");
        }
    }


    private static void throwIfMatchAlreadyExists(OrderedMatchAdapter existingMatch) throws Exception {
        if (existingMatch != null) {
            throw new Exception("Match already exists");
        }
    }
}


