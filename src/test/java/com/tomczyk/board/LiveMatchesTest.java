package com.tomczyk.board;

import com.tomczyk.board.match.Match;
import com.tomczyk.board.match.MatchEvent;
import com.tomczyk.board.match.MatchEventType;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class LiveMatchesTest {

    @Test
    public void shouldInitWithEmptyMatchesList() {
        //given
        LiveMatches liveMatches = new LiveMatches();

        //when
        //then
        assertEquals(0, liveMatches.getCurrentMatches().size());
    }


    @Test
    public void shouldAddMatchToList() throws Exception {
        //given
        LiveMatches liveMatches = new LiveMatches();
        MatchEvent matchEvent = new MatchEvent(MatchEventType.MATCH_STARTED, "Poland", "Uruguay");


        //when
        liveMatches.addMatch(matchEvent);

        //then
        assertEquals(1, liveMatches.getCurrentMatches().size());
    }

    @Test
    public void shouldThrowExceptionWhenEventTypeMismatchOccursDuringAddingMatch() {
        //given
        LiveMatches liveMatches = new LiveMatches();
        MatchEvent matchEvent = new MatchEvent(MatchEventType.MATCH_FINISHED, "Poland", "Uruguay");


        //when
        Exception exception = assertThrows(Exception.class, () -> liveMatches.addMatch(matchEvent));

        //then
        assertEquals("MatchEventType mismatch - expected: MATCH_STARTED got: MATCH_FINISHED", exception.getMessage());
    }

    @Test
    public void shouldAddMultipleMatchesToList() throws Exception {
        //given
        LiveMatches liveMatches = new LiveMatches();
        MatchEvent matchEvent1 = new MatchEvent(MatchEventType.MATCH_STARTED, "Poland", "Uruguay");
        MatchEvent matchEvent2 = new MatchEvent(MatchEventType.MATCH_STARTED, "Poland", "Uruguay");

        //when
        liveMatches.addMatch(matchEvent1);
        liveMatches.addMatch(matchEvent2);

        //then
        assertEquals(2, liveMatches.getCurrentMatches().size());
    }

    @Test
    public void shouldGetAddedMatch() throws Exception {
        //given
        LiveMatches liveMatches = new LiveMatches();
        MatchEvent matchEvent1 = new MatchEvent(MatchEventType.MATCH_STARTED, "Poland", "Uruguay");
        liveMatches.addMatch(matchEvent1);

        //when
        Match resMatch = liveMatches.getMatch("Poland", "Uruguay");

        //then
        assertEquals("Poland", resMatch.getHomeName());
        assertEquals("Uruguay", resMatch.getAwayName());
    }

    @Test
    public void shouldNotGetAddedMatchWhenDoesNotExists() {
        //given
        LiveMatches liveMatches = new LiveMatches();

        //when
        Match resMatch = liveMatches.getMatch("Poland", "Uruguay");

        //then
        assertNull(resMatch);
    }

    @Test
    public void shouldFinishMatch() throws Exception {
        //given
        LiveMatches liveMatches = new LiveMatches();
        MatchEvent matchEvent1 = new MatchEvent(MatchEventType.MATCH_STARTED, "Poland", "Uruguay");
        MatchEvent finishMatchEvent1 = new MatchEvent(MatchEventType.MATCH_FINISHED, "Poland", "Uruguay");
        liveMatches.addMatch(matchEvent1);

        //when
        liveMatches.finishMatch(finishMatchEvent1);
        Match resMatch = liveMatches.getMatch("Poland", "Uruguay");

        //then
        assertNull(resMatch);
    }

    @Test
    public void shouldThrowExceptionWhenEventTypeMismatchOccursDuringFinishingMatch() {
        //given
        LiveMatches liveMatches = new LiveMatches();
        MatchEvent matchEvent = new MatchEvent(MatchEventType.MATCH_STARTED, "Poland", "Uruguay");


        //when
        Exception exception = assertThrows(Exception.class, () -> liveMatches.finishMatch(matchEvent));

        //then
        assertEquals("MatchEventType mismatch - expected: MATCH_FINISHED got: MATCH_STARTED", exception.getMessage());
    }

    @Test
    public void shouldFinishOnlyMatchingMatch() throws Exception {
        //given
        LiveMatches liveMatches = new LiveMatches();
        MatchEvent matchEvent1 = new MatchEvent(MatchEventType.MATCH_STARTED, "Poland", "Equador");
        MatchEvent matchEvent2 = new MatchEvent(MatchEventType.MATCH_STARTED, "Poland", "Uruguay");
        MatchEvent matchEvent3 = new MatchEvent(MatchEventType.MATCH_STARTED, "Equador", "Uruguay");
        MatchEvent finishMatchEvent1 = new MatchEvent(MatchEventType.MATCH_FINISHED, "Poland", "Uruguay");

        liveMatches.addMatch(matchEvent1);
        liveMatches.addMatch(matchEvent2);
        liveMatches.addMatch(matchEvent3);

        //when
        liveMatches.finishMatch(finishMatchEvent1);
        Match finishedMatch = liveMatches.getMatch("Poland", "Uruguay");
        Match match1Res = liveMatches.getMatch("Poland", "Equador");
        Match match3Res = liveMatches.getMatch("Equador", "Uruguay");

        //then
        assertNull(finishedMatch);
        assertEquals("Poland", match1Res.getHomeName());
        assertEquals("Equador", match1Res.getAwayName());
        assertEquals("Equador", match3Res.getHomeName());
        assertEquals("Uruguay", match3Res.getAwayName());
    }

    @Test
    public void shouldThrowExceptionWhenFinishingNotExistingMatch() throws Exception {
        //given
        LiveMatches liveMatches = new LiveMatches();

        MatchEvent matchEvent1 = new MatchEvent(MatchEventType.MATCH_STARTED, "Poland", "Equador");
        MatchEvent matchEvent2 = new MatchEvent(MatchEventType.MATCH_STARTED, "Poland", "Uruguay");
        MatchEvent finishMatchEvent1 = new MatchEvent(MatchEventType.MATCH_FINISHED, "Germany", "Uruguay");


        liveMatches.addMatch(matchEvent1);
        liveMatches.addMatch(matchEvent2);

        //when
        Exception exception = assertThrows(Exception.class, () -> liveMatches.finishMatch(finishMatchEvent1));
        Match resMatch1 = liveMatches.getMatch("Poland", "Equador");
        Match resMatch2 = liveMatches.getMatch("Poland", "Uruguay");


        //then
        assertEquals("Match does not exists", exception.getMessage());
        assertEquals("Poland", resMatch1.getHomeName());
        assertEquals("Equador", resMatch1.getAwayName());
        assertEquals("Poland", resMatch2.getHomeName());
        assertEquals("Uruguay", resMatch2.getAwayName());
    }
}
