package com.tomczyk.board.match;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class MatchTest {
    final String MEXICO = "Mexico";
    final String CANADA = "Canada";

    @Test
    void shouldInitializeGameWithProperScore() {
        //given
        //when
        Match match = new Match(MEXICO, CANADA);

        //then
        assertEquals("Mexico - Canada: 0-0", match.getScore());

    }


    @Test
    void shouldUpdateHomeScore() {
        //given
        Match match = new Match(MEXICO, CANADA);

        //when
        match.scoreHome();

        //then
        assertEquals("Mexico - Canada: 1-0", match.getScore());

    }


    @Test
    void shouldUpdateAwayScore() {
        //given
        Match match = new Match(MEXICO, CANADA);

        //when
        match.scoreAway();

        //then
        assertEquals("Mexico - Canada: 0-1", match.getScore());
    }


    @Test
    void shouldCompareTwoMatches() {
        //given
        Match match1 = new Match(MEXICO, CANADA);
        Match match2 = new Match(MEXICO, CANADA);

        //when
        boolean isEqual = match1.equals(match2);

        //then
        assertTrue(isEqual);
    }


    @Test
    void shouldCheckIfMatchesHomeTeamDoesNotEqual() {
        //given
        Match match1 = new Match(MEXICO, MEXICO);
        Match match2 = new Match(CANADA, MEXICO);

        //when
        boolean isEqual = match1.equals(match2);

        //then
        assertFalse(isEqual);
    }


    @Test
    void shouldCheckIfMatchesAwayTeamDoesNotEqual() {
        //given
        Match match1 = new Match(CANADA, CANADA);
        Match match2 = new Match(CANADA, MEXICO);

        //when
        boolean isEqual = match1.equals(match2);

        //then
        assertFalse(isEqual);
    }


    @Test
    void shouldCheckIfMatchesScoreEquals() {
        //given
        Match match1 = new Match(CANADA, MEXICO);
        match1.scoreAway();
        match1.scoreAway();
        match1.scoreHome();

        Match match2 = new Match(CANADA, MEXICO);
        match2.scoreAway();
        match2.scoreAway();
        match2.scoreHome();

        //when
        boolean isEqual = match1.equals(match2);

        //then
        assertTrue(isEqual);
    }


    @Test
    void shouldCheckIfMatchesScoreDoesNotEquals() {
        //given
        Match match1 = new Match(CANADA, MEXICO);
        match1.scoreAway();
        match1.scoreHome();

        Match match2 = new Match(CANADA, MEXICO);
        match2.scoreAway();
        match2.scoreAway();
        match2.scoreHome();

        //when
        boolean isEqual = match1.equals(match2);

        //then
        assertFalse(isEqual);
    }


    @Test
    void shouldContainHomeName() {
        //given
        Match match = new Match(MEXICO, CANADA);

        //when
        //then
        assertEquals(MEXICO, match.getHomeName());
    }


    @Test
    void shouldContainAwayName() {
        //given
        Match match = new Match(MEXICO, CANADA);

        //when
        //then
        assertEquals(CANADA, match.getAwayName());
    }


    @Test
    void shouldGetHomeScore() {
        //given
        Match match = new Match(MEXICO, CANADA);
        match.scoreHome();

        //when
        //then
        assertEquals(1, match.getHomeScore());
    }


    @Test
    void shouldGetAwayScore() {
        //given
        Match match = new Match(MEXICO, CANADA);
        match.scoreAway();

        //when
        //then
        assertEquals(1, match.getAwayScore());
    }
}
