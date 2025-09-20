package com.tomczyk.board;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class LiveMatchesTest {

    @Test
    public void shouldInitWithEmptyMatchesList(){
        //given
        LiveMatches liveMatches = new LiveMatches();

        //when
        //then
        assertEquals(0, liveMatches.currentMatches.size());
    }


    @Test
    public void shouldAddMatchToList(){
        //given
        LiveMatches liveMatches = new LiveMatches();
        Match match = new Match("Poland", "Uruguay");

        //when
        liveMatches.addMatch(match);

        //then
        assertEquals(1, liveMatches.currentMatches.size());
    }

    @Test
    public void shouldAddMultipleMatchesToList(){
        //given
        LiveMatches liveMatches = new LiveMatches();
        Match match1 = new Match("Poland", "Uruguay");
        Match match2 = new Match("Poland", "Uruguay");

        //when
        liveMatches.addMatch(match1);
        liveMatches.addMatch(match2);

        //then
        assertEquals(2, liveMatches.currentMatches.size());
    }

    @Test
    public void shouldGetAddedMatch(){
        //given
        LiveMatches liveMatches = new LiveMatches();
        Match match1 = new Match("Poland", "Uruguay");

        //when
        Match resMatch = liveMatches.getMatch("Poland", "Uruguay");

        //then
        assertEquals(match1, resMatch);
    }


    @Test
    public void shouldNotGetAddedMatchWhenDoesNotExists(){
        //given
        LiveMatches liveMatches = new LiveMatches();

        //when
        Match resMatch = liveMatches.getMatch("Poland", "Uruguay");

        //then
        assertEquals(null, resMatch);
    }
}
