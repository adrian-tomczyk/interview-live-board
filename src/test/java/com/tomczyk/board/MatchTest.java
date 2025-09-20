package com.tomczyk.board;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class MatchTest {

    @Test
    void shouldInitializeGameWithProperScore() {
        //given
        //when
        Match match = new Match("Mexico", "Canada");

        //then
        assertEquals("Mexico - Canada: 0-0", match.getScore());

    }

    @Test
    void shouldUpdateHomeScore() {
        //given
        Match match = new Match("Mexico", "Canada");

        //when
        match.scoreHome();

        //then
        assertEquals("Mexico - Canada: 1-0", match.getScore());

    }

    @Test
    void shouldUpdateAwayScore() {
        //given
        Match match = new Match("Mexico", "Canada");

        //when
        match.scoreAway();

        //then
        assertEquals("Mexico - Canada: 0-1", match.getScore());
    }

    @Test
    void shouldCompareTwoMatches(){
        //given
        Match match1 = new Match("Mexico", "Canada");
        Match match2 = new Match("Mexico", "Canada");

        //when
        boolean isEqual = match1.equals(match2);

        //then
        assertTrue(isEqual);
    }

    @Test
    void shouldCheckIfMatchesHomeTeamDoesNotEqual(){
        //given
        Match match1 = new Match("Mexico", "Mexico");
        Match match2 = new Match("Canada", "Mexico");

        //when
        boolean isEqual = match1.equals(match2);

        //then
        assertFalse(isEqual);
    }

    @Test
    void shouldCheckIfMatchesAwayTeamDoesNotEqual(){
        //given
        Match match1 = new Match("Canada", "Canada");
        Match match2 = new Match("Canada", "Mexico");

        //when
        boolean isEqual = match1.equals(match2);

        //then
        assertFalse(isEqual);
    }

    @Test
    void shouldCheckIfMatchesScoreEquals(){
        //given
        Match match1 = new Match("Canada", "Mexico");
        match1.scoreAway();
        match1.scoreAway();
        match1.scoreHome();

        Match match2 = new Match("Canada", "Mexico");
        match2.scoreAway();
        match2.scoreAway();
        match2.scoreHome();

        //when
        boolean isEqual = match1.equals(match2);

        //then
        assertTrue(isEqual);
    }


    @Test
    void shouldCheckIfMatchesScoreDoesNotEquals(){
        //given
        Match match1 = new Match("Canada", "Mexico");
        match1.scoreAway();
        match1.scoreHome();

        Match match2 = new Match("Canada", "Mexico");
        match2.scoreAway();
        match2.scoreAway();
        match2.scoreHome();

        //when
        boolean isEqual = match1.equals(match2);

        //then
        assertFalse(isEqual);
    }
}
