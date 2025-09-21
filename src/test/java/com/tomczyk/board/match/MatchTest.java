package com.tomczyk.board.match;

import com.tomczyk.board.utils.Country;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;


public class MatchTest {

    @Test
    void shouldInitializeGameWithProperScore() {
        //given
        //when
        Match match = new Match(Country.MEXICO, Country.CANADA);

        //then
        assertEquals("Mexico - Canada: 0-0", match.getScore());

    }


    @Test
    void shouldUpdateHomeScore() {
        //given
        Match match = new Match(Country.MEXICO, Country.CANADA);

        //when
        match.updateScore(1,0);

        //then
        assertEquals("Mexico - Canada: 1-0", match.getScore());
    }


    @Test
    void shouldUpdateAwayScore() {
        //given
        Match match = new Match(Country.MEXICO, Country.CANADA);

        //when
        match.updateScore(0,1);

        //then
        assertEquals("Mexico - Canada: 0-1", match.getScore());
    }


    @Test
    void shouldNotUpdateScoreWhenHomeScoreIsBelowZero() {
        //given
        Match match = new Match(Country.MEXICO, Country.CANADA);

        //when
        match.updateScore(-2,1);

        //then
        assertEquals("Mexico - Canada: 0-0", match.getScore());
    }


    @Test
    void shouldNotUpdateScoreWhenAwayScoreIsBelowZero() {
        //given
        Match match = new Match(Country.MEXICO, Country.CANADA);

        //when
        match.updateScore(2,-1);

        //then
        assertEquals("Mexico - Canada: 0-0", match.getScore());
    }


    @Test
    void shouldCompareTwoMatches() {
        //given
        Match match1 = new Match(Country.MEXICO, Country.CANADA);
        Match match2 = new Match(Country.MEXICO, Country.CANADA);

        //when
        boolean isEqual = match1.equals(match2);

        //then
        assertTrue(isEqual);
    }


    @Test
    void shouldCheckIfMatchesHomeTeamDoesNotEqual() {
        //given
        Match match1 = new Match(Country.MEXICO, Country.MEXICO);
        Match match2 = new Match(Country.CANADA, Country.MEXICO);

        //when
        boolean isEqual = match1.equals(match2);

        //then
        assertFalse(isEqual);
    }


    @Test
    void shouldCheckIfMatchesAwayTeamDoesNotEqual() {
        //given
        Match match1 = new Match(Country.CANADA, Country.CANADA);
        Match match2 = new Match(Country.CANADA, Country.MEXICO);

        //when
        boolean isEqual = match1.equals(match2);

        //then
        assertFalse(isEqual);
    }


    @Test
    void shouldCheckIfMatchesScoreEquals() {
        //given
        Match match1 = new Match(Country.CANADA, Country.MEXICO);
        match1.updateScore(2,1);


        Match match2 = new Match(Country.CANADA, Country.MEXICO);
        match2.updateScore(2,1);


        //when
        boolean isEqual = match1.equals(match2);

        //then
        assertTrue(isEqual);
    }


    @Test
    void shouldCheckIfMatchesScoreDoesNotEquals() {
        //given
        Match match1 = new Match(Country.CANADA, Country.MEXICO);
        match1.updateScore(2,1);


        Match match2 = new Match(Country.CANADA, Country.MEXICO);
        match1.updateScore(2,3);


        //when
        boolean isEqual = match1.equals(match2);

        //then
        assertFalse(isEqual);
    }


    @Test
    void shouldContainHomeName() {
        //given
        Match match = new Match(Country.MEXICO, Country.CANADA);

        //when
        //then
        assertEquals(Country.MEXICO, match.getHomeName());
    }


    @Test
    void shouldContainAwayName() {
        //given
        Match match = new Match(Country.MEXICO, Country.CANADA);

        //when
        //then
        assertEquals(Country.CANADA, match.getAwayName());
    }


    @Test
    void shouldGetHomeScore() {
        //given
        Match match = new Match(Country.MEXICO, Country.CANADA);
        match.updateScore(1,0);


        //when
        //then
        assertEquals(1, match.getHomeScore());
    }


    @Test
    void shouldGetAwayScore() {
        //given
        Match match = new Match(Country.MEXICO, Country.CANADA);
        match.updateScore(0,1);


        //when
        //then
        assertEquals(1, match.getAwayScore());
    }
}
