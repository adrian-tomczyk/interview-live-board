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
    void shouldUpdateHomeScore() throws Exception {
        //given
        Match match = new Match(Country.MEXICO, Country.CANADA);
        MatchEvent matchEvent = new MatchEvent(MatchEventType.HOME_TEAM_SCORES, Country.MEXICO, Country.CANADA);

        //when
        match.handleMatchEvent(matchEvent);

        //then
        assertEquals("Mexico - Canada: 1-0", match.getScore());
    }


    @Test
    void shouldUpdateAwayScore() throws Exception {
        //given
        Match match = new Match(Country.MEXICO, Country.CANADA);
        MatchEvent matchEvent = new MatchEvent(MatchEventType.AWAY_TEAM_SCORES, Country.MEXICO, Country.CANADA);

        //when
        match.handleMatchEvent(matchEvent);

        //then
        assertEquals("Mexico - Canada: 0-1", match.getScore());
    }


    @Test
    void shouldThrowExceptionWhenEventHasWrongHomeTeam() {
        //given
        Match match = new Match(Country.MEXICO, Country.CANADA);
        MatchEvent matchEvent = new MatchEvent(MatchEventType.AWAY_TEAM_SCORES, Country.POLAND, Country.CANADA);

        //when
        Exception exception = assertThrows(Exception.class, () -> match.handleMatchEvent(matchEvent));

        //then
        assertEquals("Match teams do not match", exception.getMessage());
    }


    @Test
    void shouldThrowExceptionWhenEventHasWrongAwayTeam() {
        //given
        Match match = new Match(Country.MEXICO, Country.CANADA);
        MatchEvent matchEvent = new MatchEvent(MatchEventType.AWAY_TEAM_SCORES, Country.MEXICO, Country.POLAND);

        //when
        Exception exception = assertThrows(Exception.class, () -> match.handleMatchEvent(matchEvent));

        //then
        assertEquals("Match teams do not match", exception.getMessage());
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
        match1.scoreAway();
        match1.scoreAway();
        match1.scoreHome();

        Match match2 = new Match(Country.CANADA, Country.MEXICO);
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
        Match match1 = new Match(Country.CANADA, Country.MEXICO);
        match1.scoreAway();
        match1.scoreHome();

        Match match2 = new Match(Country.CANADA, Country.MEXICO);
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
        match.scoreHome();

        //when
        //then
        assertEquals(1, match.getHomeScore());
    }


    @Test
    void shouldGetAwayScore() {
        //given
        Match match = new Match(Country.MEXICO, Country.CANADA);
        match.scoreAway();

        //when
        //then
        assertEquals(1, match.getAwayScore());
    }
}
