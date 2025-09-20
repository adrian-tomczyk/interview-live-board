package com.tomczyk.board;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

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
}
