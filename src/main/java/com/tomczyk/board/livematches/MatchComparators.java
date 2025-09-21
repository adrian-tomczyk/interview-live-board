package com.tomczyk.board.livematches;

import com.tomczyk.board.match.Match;

public class MatchComparators {
    public static boolean areTeamsEqual(Match match, String home, String away) {
        return match.getHomeName().equals(home) && match.getAwayName().equals(away);
    }


    public static boolean wasCreatedLater(Match match1, Match match2) {
        return match1.getCreationDate().after(match2.getCreationDate());
    }


    public static boolean isScoreSumEqual(Match matchWrapper, Match matchAtIndex) {
        return getOrderedMatchAdapterScoreSum(matchAtIndex) == getOrderedMatchAdapterScoreSum(matchWrapper);
    }


    public static boolean isScoreSumLower(Match match1, Match match2) {
        return getOrderedMatchAdapterScoreSum(match1) < getOrderedMatchAdapterScoreSum(match2);
    }


    public static int getOrderedMatchAdapterScoreSum(Match match) {
        return match.getHomeScore() + match.getAwayScore();
    }
}