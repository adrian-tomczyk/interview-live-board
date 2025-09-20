package com.tomczyk.board.livematches;

import com.tomczyk.board.match.Match;
import com.tomczyk.board.match.MatchEvent;
import com.tomczyk.board.match.MatchEventType;

import java.util.ArrayList;
import java.util.List;

public class LiveMatches {
    final ArrayList<OrderedMatchAdapter> matches;
    private Integer matchesCount;

    private boolean areTeamsEqual(OrderedMatchAdapter orderedMatchAdapter, String home, String away) {
        return orderedMatchAdapter.match().getHomeName().equals(home) && orderedMatchAdapter.match().getAwayName().equals(away);
    }

    private static void throwUnexpectedEventType(List<MatchEventType> expectedMatchEventType, MatchEvent matchEvent) throws Exception {
        if (!expectedMatchEventType.contains(matchEvent.matchEventType())) {
            throw new Exception("MatchEventType mismatch - expected: " + expectedMatchEventType.stream().map(Enum::toString).toList() + " got: " + matchEvent.matchEventType().toString());
        }
    }

    public LiveMatches() {
        matches = new ArrayList<>();
        matchesCount = 0;
    }

    public List<Match> getCurrentMatches() {
        return matches.stream().map(OrderedMatchAdapter::match).toList();
    }

    public void addMatch(MatchEvent matchEvent) throws Exception {
        throwUnexpectedEventType(List.of(MatchEventType.MATCH_STARTED), matchEvent);

        Match existingMatch = getMatch(matchEvent.home(), matchEvent.away());

        if (existingMatch != null) {
            throw new Exception("Match already exists");
        }

        Match match = new Match(matchEvent.home(), matchEvent.away());
        OrderedMatchAdapter orderedMatchAdapter = new OrderedMatchAdapter(match,matchesCount);

        matches.addLast(orderedMatchAdapter);
        matchesCount++;
    }

    public Match getMatch(String home, String away) {
        OrderedMatchAdapter orderedMatchAdapter = getOrderedMatchAdapter(home,away);
        if(orderedMatchAdapter == null) return null;

        return getOrderedMatchAdapter(home,away).match();
    }

    private OrderedMatchAdapter getOrderedMatchAdapter(String home, String away) {
        return matches.stream()
                .filter(match -> areTeamsEqual(match, home, away))
                .findFirst()
                .orElse(null);
    }

    public void finishMatch(MatchEvent matchEvent) throws Exception {
        throwUnexpectedEventType(List.of(MatchEventType.MATCH_FINISHED), matchEvent);

        OrderedMatchAdapter orderedMatchAdapter = getOrderedMatchAdapter(matchEvent.home(), matchEvent.away());

        if (orderedMatchAdapter == null) {
            throw new Exception("Match does not exists");
        }

        matches.remove(orderedMatchAdapter);
    }

    public void updateMatchScore(MatchEvent matchEvent) throws Exception {
        throwUnexpectedEventType(List.of(MatchEventType.HOME_TEAM_SCORES, MatchEventType.AWAY_TEAM_SCORES), matchEvent);

        OrderedMatchAdapter orderedMatchAdapter = getOrderedMatchAdapter(matchEvent.home(), matchEvent.away());

        switch (matchEvent.matchEventType()) {
            case HOME_TEAM_SCORES -> orderedMatchAdapter.match().scoreHome();
            case AWAY_TEAM_SCORES -> orderedMatchAdapter.match().scoreAway();
        }

        updateMatchPosition(orderedMatchAdapter);
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

            if(shouldMatchBePushedDown(orderedMatchAdapter, matchAtIndex)) return i;
        }

        return currentIndex;
    }

    private static boolean shouldMatchBePushedDown(OrderedMatchAdapter orderedMatchAdapter, OrderedMatchAdapter matchAtIndex) {
        return getOrderedMatchAdapterScoreSum(matchAtIndex) < getOrderedMatchAdapterScoreSum(orderedMatchAdapter) || (getOrderedMatchAdapterScoreSum(matchAtIndex) == getOrderedMatchAdapterScoreSum(orderedMatchAdapter) && matchAtIndex.order() < orderedMatchAdapter.order());
    }

    private static int getOrderedMatchAdapterScoreSum(OrderedMatchAdapter orderedMatchAdapter) {
        return orderedMatchAdapter.match().getHomeScore() + orderedMatchAdapter.match().getAwayScore();
    }
}


