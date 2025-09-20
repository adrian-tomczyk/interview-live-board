package com.tomczyk.board;

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
    public void shouldAddMatchToList() {
        //given
        LiveMatches liveMatches = new LiveMatches();
        Match match = new Match("Poland", "Uruguay");

        //when
        liveMatches.addMatch(match);

        //then
        assertEquals(1, liveMatches.getCurrentMatches().size());
    }

    @Test
    public void shouldAddMultipleMatchesToList() {
        //given
        LiveMatches liveMatches = new LiveMatches();
        Match match1 = new Match("Poland", "Uruguay");
        Match match2 = new Match("Poland", "Uruguay");

        //when
        liveMatches.addMatch(match1);
        liveMatches.addMatch(match2);

        //then
        assertEquals(2, liveMatches.getCurrentMatches().size());
    }

    @Test
    public void shouldGetAddedMatch() {
        //given
        LiveMatches liveMatches = new LiveMatches();
        Match match1 = new Match("Poland", "Uruguay");
        liveMatches.addMatch(match1);

        //when
        Match resMatch = liveMatches.getMatch("Poland", "Uruguay");

        //then
        assertEquals(match1, resMatch);
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
        Match match1 = new Match("Poland", "Uruguay");
        liveMatches.addMatch(match1);

        //when
        liveMatches.finishMatch("Poland", "Uruguay");
        Match resMatch = liveMatches.getMatch("Poland", "Uruguay");

        //then
        assertNull(resMatch);
    }

    @Test
    public void shouldFinishOnlyMatchingMatch() throws Exception {
        //given
        LiveMatches liveMatches = new LiveMatches();
        Match match1 = new Match("Poland", "Equador");
        Match match2 = new Match("Poland", "Uruguay");
        Match match3 = new Match("Equador", "Uruguay");

        liveMatches.addMatch(match1);
        liveMatches.addMatch(match2);
        liveMatches.addMatch(match3);

        //when
        liveMatches.finishMatch("Poland", "Uruguay");
        Match finishedMatch = liveMatches.getMatch("Poland", "Uruguay");
        Match match1Res = liveMatches.getMatch("Poland", "Equador");
        Match match3Res = liveMatches.getMatch("Equador", "Uruguay");

        //then
        assertNull(finishedMatch);
        assertEquals(match1, match1Res);
        assertEquals(match3, match3Res);
    }

    @Test
    public void shouldThrowExceptionWhenFinishingNotExistingMatch() {
        //given
        LiveMatches liveMatches = new LiveMatches();
        Match match1 = new Match("Poland", "Equador");
        Match match2 = new Match("Poland", "Uruguay");

        liveMatches.addMatch(match1);
        liveMatches.addMatch(match2);

        //when
        Exception exception = assertThrows(Exception.class, () -> liveMatches.finishMatch("Germany", "Uruguay"));
        Match resMatch1 = liveMatches.getMatch("Poland", "Equador");
        Match resMatch2 = liveMatches.getMatch("Poland", "Uruguay");


        //then
        assertEquals("Match does not exists", exception.getMessage());
        assertEquals(match1, resMatch1);
        assertEquals(match2, resMatch2);
    }
}
