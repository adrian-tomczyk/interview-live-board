package com.tomczyk.board.livematches;

import com.tomczyk.board.match.Match;
import com.tomczyk.board.match.MatchEvent;
import com.tomczyk.board.match.MatchEventType;

import java.util.ArrayList;
import java.util.List;

public class LiveMatches {
    final ArrayList<OrderedMatchAdapter> matches;
    private Integer matchesCount;


    public LiveMatches() {
        matches = new ArrayList<>();
        matchesCount = 0;
    }


    public void addMatch(MatchEvent matchEvent) throws Exception {
        throwIfUnexpectedEventType(List.of(MatchEventType.MATCH_STARTED), matchEvent);

        Match existingMatch = getMatch(matchEvent.home(), matchEvent.away());

        if (existingMatch != null) {
            throw new Exception("Match already exists");
        }

        Match match = new Match(matchEvent.home(), matchEvent.away());
        OrderedMatchAdapter orderedMatchAdapter = new OrderedMatchAdapter(match,matchesCount);

        matches.addLast(orderedMatchAdapter);
        matchesCount++;
    }


    public void finishMatch(MatchEvent matchEvent) throws Exception {
        throwIfUnexpectedEventType(List.of(MatchEventType.MATCH_FINISHED), matchEvent);

        OrderedMatchAdapter orderedMatchAdapter = getOrderedMatchAdapter(matchEvent.home(), matchEvent.away());

        throwIfMatchDoesNotExists(orderedMatchAdapter);

        matches.remove(orderedMatchAdapter);
    }


    public List<Match> getCurrentMatches() {
        return matches.stream().map(OrderedMatchAdapter::match).toList();
    }


    public Match getMatch(String home, String away) {
        OrderedMatchAdapter orderedMatchAdapter = getOrderedMatchAdapter(home,away);
        if(orderedMatchAdapter == null) return null;

        return getOrderedMatchAdapter(home,away).match();
    }


    public void updateMatchScore(MatchEvent matchEvent) throws Exception {
        throwIfUnexpectedEventType(List.of(MatchEventType.HOME_TEAM_SCORES, MatchEventType.AWAY_TEAM_SCORES), matchEvent);

        OrderedMatchAdapter orderedMatchAdapter = getOrderedMatchAdapter(matchEvent.home(), matchEvent.away());

        throwIfMatchDoesNotExists(orderedMatchAdapter);

        switch (matchEvent.matchEventType()) {
            case HOME_TEAM_SCORES -> orderedMatchAdapter.match().scoreHome();
            case AWAY_TEAM_SCORES -> orderedMatchAdapter.match().scoreAway();
        }

        updateMatchPosition(orderedMatchAdapter);
    }


    private boolean areTeamsEqual(OrderedMatchAdapter orderedMatchAdapter, String home, String away) {
        return orderedMatchAdapter.match().getHomeName().equals(home) && orderedMatchAdapter.match().getAwayName().equals(away);
    }


    private static void throwIfUnexpectedEventType(List<MatchEventType> expectedMatchEventType, MatchEvent matchEvent) throws Exception {
        if (!expectedMatchEventType.contains(matchEvent.matchEventType())) {
            throw new Exception("MatchEventType mismatch - expected: " + expectedMatchEventType.stream().map(Enum::toString).toList() + " got: " + matchEvent.matchEventType().toString());
        }
    }


    private static void throwIfMatchDoesNotExists(OrderedMatchAdapter orderedMatchAdapter) throws Exception {
        if (orderedMatchAdapter == null) {
            throw new Exception("Match does not exists");
        }
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

        for (int i = 0; i < currentIndex; ++i) {
            OrderedMatchAdapter matchAtIndex = matches.get(i);

            if(shouldMatchBeInsertedAtPosition(orderedMatchAdapter, matchAtIndex)) return i;
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

}


