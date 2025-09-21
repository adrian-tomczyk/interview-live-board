package com.tomczyk.board.livematches;

import com.tomczyk.board.match.exceptions.MatchAlreadyExistsException;
import com.tomczyk.board.match.exceptions.MatchDoesNotExistException;
import com.tomczyk.board.utils.Country;
import com.tomczyk.board.match.Match;
import org.junit.jupiter.api.Test;

import java.util.stream.IntStream;

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

        //when
        createMatch(liveMatches, Country.POLAND, Country.URUGUAY);

        //then
        assertEquals(1, liveMatches.getCurrentMatches().size());
    }


    @Test
    public void shouldThrowExceptionWhenMatchAlreadyExists() {
        //given
        LiveMatches liveMatches = new LiveMatches();
        createMatch(liveMatches, Country.POLAND, Country.URUGUAY);


        //when
        MatchAlreadyExistsException exception = assertThrows(MatchAlreadyExistsException.class, () -> createMatch(liveMatches, Country.POLAND, Country.URUGUAY));

        //then
        assertEquals("Match already exists", exception.getMessage());
        assertEquals(1, liveMatches.getCurrentMatches().size());
    }


    @Test
    public void shouldAddMultipleMatchesToList() {
        //given
        LiveMatches liveMatches = new LiveMatches();

        //when
        createMatch(liveMatches, Country.POLAND, Country.URUGUAY);
        createMatch(liveMatches, Country.GERMANY, Country.FRANCE);

        //then
        assertEquals(2, liveMatches.getCurrentMatches().size());
    }


    @Test
    public void shouldGetAddedMatch() {
        //given
        LiveMatches liveMatches = new LiveMatches();
        createMatch(liveMatches, Country.POLAND, Country.URUGUAY);

        //when
        Match resMatch = liveMatches.getMatchByTeamNames(Country.POLAND, Country.URUGUAY);

        //then
        assertEquals(Country.POLAND, resMatch.getHomeName());
        assertEquals(Country.URUGUAY, resMatch.getAwayName());
    }


    @Test
    public void shouldNotReturnAddedMatchWhenDoesNotExists() {
        //given
        LiveMatches liveMatches = new LiveMatches();

        //when
        Match resMatch = liveMatches.getMatchByTeamNames(Country.POLAND, Country.URUGUAY);

        //then
        assertNull(resMatch);
    }


    @Test
    public void shouldFinishMatch() {
        //given
        LiveMatches liveMatches = new LiveMatches();
        createMatch(liveMatches, Country.POLAND, Country.URUGUAY);

        //when
        finishMatch(liveMatches, Country.POLAND, Country.URUGUAY);
        Match resMatch = liveMatches.getMatchByTeamNames(Country.POLAND, Country.URUGUAY);

        //then
        assertNull(resMatch);
    }


    @Test
    public void shouldFinishOnlyMatchingMatch() {
        //given
        LiveMatches liveMatches = new LiveMatches();
        createMatch(liveMatches, Country.POLAND, Country.EQUADOR);
        createMatch(liveMatches, Country.POLAND, Country.URUGUAY);
        createMatch(liveMatches, Country.EQUADOR, Country.URUGUAY);

        //when
        finishMatch(liveMatches, Country.POLAND, Country.URUGUAY);

        Match finishedMatch = liveMatches.getMatchByTeamNames(Country.POLAND, Country.URUGUAY);
        Match match1Res = liveMatches.getMatchByTeamNames(Country.POLAND, Country.EQUADOR);
        Match match3Res = liveMatches.getMatchByTeamNames(Country.EQUADOR, Country.URUGUAY);

        //then
        assertNull(finishedMatch);
        assertEquals(Country.POLAND, match1Res.getHomeName());
        assertEquals(Country.EQUADOR, match1Res.getAwayName());
        assertEquals(Country.EQUADOR, match3Res.getHomeName());
        assertEquals(Country.URUGUAY, match3Res.getAwayName());
    }


    @Test
    public void shouldThrowExceptionWhenFinishingNotExistingMatch() {
        //given
        LiveMatches liveMatches = new LiveMatches();

        createMatch(liveMatches, Country.POLAND, Country.EQUADOR);
        createMatch(liveMatches, Country.POLAND, Country.URUGUAY);

        //when
        MatchDoesNotExistException exception = assertThrows(MatchDoesNotExistException.class, () -> liveMatches.finishMatch(Country.GERMANY, Country.URUGUAY));
        Match resMatch1 = liveMatches.getMatchByTeamNames(Country.POLAND, Country.EQUADOR);
        Match resMatch2 = liveMatches.getMatchByTeamNames(Country.POLAND, Country.URUGUAY);


        //then
        assertEquals("Match does not exist", exception.getMessage());
        assertEquals(Country.POLAND, resMatch1.getHomeName());
        assertEquals(Country.EQUADOR, resMatch1.getAwayName());
        assertEquals(Country.POLAND, resMatch2.getHomeName());
        assertEquals(Country.URUGUAY, resMatch2.getAwayName());
    }


    @Test
    public void shouldUpdateMatchHomeScore() {
        //given
        LiveMatches liveMatches = new LiveMatches();

        createMatch(liveMatches, Country.POLAND, Country.URUGUAY);

        //when
        liveMatches.updateScore(Country.POLAND, Country.URUGUAY, 2 ,0);

        //then
        Match match = liveMatches.getMatchByTeamNames(Country.POLAND, Country.URUGUAY);
        assertEquals(1, match.getHomeScore());
    }


    @Test
    public void shouldUpdateMatchAwayScore() {
        //given
        LiveMatches liveMatches = new LiveMatches();

        createMatch(liveMatches, Country.POLAND, Country.URUGUAY);

        //when
        liveMatches.updateScore(Country.POLAND, Country.URUGUAY, 0, 2);

        //then
        Match match = liveMatches.getMatchByTeamNames(Country.POLAND, Country.URUGUAY);
        assertEquals(1, match.getAwayScore());
    }


    @Test
    public void shouldThrowExceptionWhenMatchDoesNotExistOnUpdateScore() {
        //given
        LiveMatches liveMatches = new LiveMatches();

        //when
        MatchDoesNotExistException exception = assertThrows(MatchDoesNotExistException.class, () -> liveMatches.scoreAway(Country.POLAND, Country.URUGUAY));

        //then
        assertEquals("Match does not exist", exception.getMessage());
    }


    @Test
    public void shouldOrderMatchesByHighestScoreSum() {
        //given
        LiveMatches liveMatches = new LiveMatches();

        createMatch(liveMatches, Country.POLAND, Country.URUGUAY);
        createMatch(liveMatches, Country.MEXICO, Country.CANADA);
        createMatch(liveMatches, Country.GERMANY, Country.FRANCE);
        createMatch(liveMatches, Country.ARGENTINA, Country.AUSTRALIA);

        liveMatches.updateScore(Country.MEXICO, Country.CANADA, 3, 0);
        liveMatches.updateScore(Country.GERMANY, Country.FRANCE, 0, 4);
        liveMatches.updateScore(Country.ARGENTINA, Country.ARGENTINA, 3, 3);

        //when
        Match match1 = liveMatches.getCurrentMatches().getFirst();
        Match match2 = liveMatches.getCurrentMatches().get(1);
        Match match3 = liveMatches.getCurrentMatches().get(2);
        Match match4 = liveMatches.getCurrentMatches().get(3);


        //then
        assertEquals(Country.ARGENTINA, match1.getHomeName());
        assertEquals(Country.AUSTRALIA, match1.getAwayName());

        assertEquals(Country.GERMANY, match2.getHomeName());
        assertEquals(Country.FRANCE, match2.getAwayName());

        assertEquals(Country.MEXICO, match3.getHomeName());
        assertEquals(Country.CANADA, match3.getAwayName());

        assertEquals(Country.POLAND, match4.getHomeName());
        assertEquals(Country.URUGUAY, match4.getAwayName());
    }


    @Test
    public void shouldOrderMatchesByStartEvent() {
        //given
        LiveMatches liveMatches = new LiveMatches();

        createMatch(liveMatches, Country.POLAND, Country.URUGUAY);
        createMatch(liveMatches, Country.MEXICO, Country.CANADA);
        createMatch(liveMatches, Country.GERMANY, Country.FRANCE);
        createMatch(liveMatches, Country.ARGENTINA, Country.AUSTRALIA);

        //when
        Match match1 = liveMatches.getCurrentMatches().getFirst();
        Match match2 = liveMatches.getCurrentMatches().get(1);
        Match match3 = liveMatches.getCurrentMatches().get(2);
        Match match4 = liveMatches.getCurrentMatches().get(3);


        //then
        assertEquals(Country.POLAND, match1.getHomeName());
        assertEquals(Country.URUGUAY, match1.getAwayName());

        assertEquals(Country.MEXICO, match2.getHomeName());
        assertEquals(Country.CANADA, match2.getAwayName());

        assertEquals(Country.GERMANY, match3.getHomeName());
        assertEquals(Country.FRANCE, match3.getAwayName());

        assertEquals(Country.ARGENTINA, match4.getHomeName());
        assertEquals(Country.AUSTRALIA, match4.getAwayName());
    }


    @Test
    public void shouldOrderMatchesByScoreAndThenStartEvent() {
        //given
        LiveMatches liveMatches = new LiveMatches();

        createMatch(liveMatches, Country.MEXICO, Country.CANADA);
        createMatch(liveMatches, Country.SPAIN, Country.BRAZIL);
        createMatch(liveMatches, Country.GERMANY, Country.FRANCE);
        createMatch(liveMatches, Country.URUGUAY, Country.ITALY);
        createMatch(liveMatches, Country.ARGENTINA, Country.AUSTRALIA);

        liveMatches.updateScore(Country.MEXICO, Country.CANADA, 0, 5);
        liveMatches.updateScore(Country.SPAIN, Country.BRAZIL, 10, 2);
        liveMatches.updateScore(Country.GERMANY, Country.FRANCE, 2, 2);
        liveMatches.updateScore(Country.URUGUAY, Country.ITALY, 6, 6);
        liveMatches.updateScore(Country.ARGENTINA, Country.AUSTRALIA, 3, 1);

        //when
        Match match1 = liveMatches.getCurrentMatches().getFirst();
        Match match2 = liveMatches.getCurrentMatches().get(1);
        Match match3 = liveMatches.getCurrentMatches().get(2);
        Match match4 = liveMatches.getCurrentMatches().get(3);
        Match match5 = liveMatches.getCurrentMatches().get(4);

        //then
        assertEquals(Country.URUGUAY, match1.getHomeName());
        assertEquals(Country.ITALY, match1.getAwayName());

        assertEquals(Country.SPAIN, match2.getHomeName());
        assertEquals(Country.BRAZIL, match2.getAwayName());

        assertEquals(Country.MEXICO, match3.getHomeName());
        assertEquals(Country.CANADA, match3.getAwayName());

        assertEquals(Country.ARGENTINA, match4.getHomeName());
        assertEquals(Country.AUSTRALIA, match4.getAwayName());

        assertEquals(Country.GERMANY, match5.getHomeName());
        assertEquals(Country.FRANCE, match5.getAwayName());
    }


    private static void createMatch(LiveMatches liveMatches, String home, String away) {
        Match match = new Match(home, away);
        liveMatches.addMatch(match);
    }


    private static void finishMatch(LiveMatches liveMatches, String home, String away) {
        liveMatches.finishMatch(home, away);
    }

}
