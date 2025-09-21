package com.tomczyk.board.livematches;

import com.tomczyk.board.utils.Country;
import com.tomczyk.board.match.Match;
import com.tomczyk.board.match.MatchEvent;
import com.tomczyk.board.match.MatchEventType;
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
    public void shouldAddMatchToList() throws Exception {
        //given
        LiveMatches liveMatches = new LiveMatches();


        //when
        createMatch(liveMatches, Country.POLAND, Country.URUGUAY);

        //then
        assertEquals(1, liveMatches.getCurrentMatches().size());
    }


    @Test
    public void shouldThrowExceptionWhenMatchAlreadyExists() throws Exception {
        //given
        LiveMatches liveMatches = new LiveMatches();
        createMatch(liveMatches, Country.POLAND, Country.URUGUAY);


        //when
        Exception exception = assertThrows(Exception.class, () -> createMatch(liveMatches, Country.POLAND, Country.URUGUAY));

        //then
        assertEquals("Match already exists", exception.getMessage());
        assertEquals(1, liveMatches.getCurrentMatches().size());
    }


    @Test
    public void shouldThrowExceptionWhenEventTypeMismatchOccursDuringAddingMatch() {
        //given
        LiveMatches liveMatches = new LiveMatches();
        MatchEvent matchEvent = new MatchEvent(MatchEventType.MATCH_FINISHED, Country.POLAND, Country.URUGUAY);


        //when
        Exception exception = assertThrows(Exception.class, () -> liveMatches.addMatch(matchEvent));

        //then
        assertEquals("MatchEventType mismatch - expected: [MATCH_STARTED] got: MATCH_FINISHED", exception.getMessage());
    }


    @Test
    public void shouldAddMultipleMatchesToList() throws Exception {
        //given
        LiveMatches liveMatches = new LiveMatches();

        //when
        createMatch(liveMatches, Country.POLAND, Country.URUGUAY);
        createMatch(liveMatches, Country.GERMANY, Country.FRANCE);

        //then
        assertEquals(2, liveMatches.getCurrentMatches().size());
    }


    @Test
    public void shouldGetAddedMatch() throws Exception {
        //given
        LiveMatches liveMatches = new LiveMatches();
        createMatch(liveMatches, Country.POLAND, Country.URUGUAY);

        //when
        Match resMatch = liveMatches.getMatch(Country.POLAND, Country.URUGUAY);

        //then
        assertEquals(Country.POLAND, resMatch.getHomeName());
        assertEquals(Country.URUGUAY, resMatch.getAwayName());
    }


    @Test
    public void shouldNotGetAddedMatchWhenDoesNotExists() {
        //given
        LiveMatches liveMatches = new LiveMatches();

        //when
        Match resMatch = liveMatches.getMatch(Country.POLAND, Country.URUGUAY);

        //then
        assertNull(resMatch);
    }


    @Test
    public void shouldFinishMatch() throws Exception {
        //given
        LiveMatches liveMatches = new LiveMatches();
        createMatch(liveMatches, Country.POLAND, Country.URUGUAY);

        //when
        finishMatch(liveMatches, Country.POLAND, Country.URUGUAY);
        Match resMatch = liveMatches.getMatch(Country.POLAND, Country.URUGUAY);

        //then
        assertNull(resMatch);
    }


    @Test
    public void shouldThrowExceptionWhenEventTypeMismatchOccursDuringFinishingMatch() {
        //given
        LiveMatches liveMatches = new LiveMatches();
        MatchEvent matchEvent = new MatchEvent(MatchEventType.MATCH_STARTED, Country.POLAND, Country.URUGUAY);

        //when
        Exception exception = assertThrows(Exception.class, () -> liveMatches.finishMatch(matchEvent));

        //then
        assertEquals("MatchEventType mismatch - expected: [MATCH_FINISHED] got: MATCH_STARTED", exception.getMessage());
    }


    @Test
    public void shouldFinishOnlyMatchingMatch() throws Exception {
        //given
        LiveMatches liveMatches = new LiveMatches();
        createMatch(liveMatches, Country.POLAND, Country.EQUADOR);
        createMatch(liveMatches, Country.POLAND, Country.URUGUAY);
        createMatch(liveMatches, Country.EQUADOR, Country.URUGUAY);

        //when
        finishMatch(liveMatches, Country.POLAND, Country.URUGUAY);

        Match finishedMatch = liveMatches.getMatch(Country.POLAND, Country.URUGUAY);
        Match match1Res = liveMatches.getMatch(Country.POLAND, Country.EQUADOR);
        Match match3Res = liveMatches.getMatch(Country.EQUADOR, Country.URUGUAY);

        //then
        assertNull(finishedMatch);
        assertEquals(Country.POLAND, match1Res.getHomeName());
        assertEquals(Country.EQUADOR, match1Res.getAwayName());
        assertEquals(Country.EQUADOR, match3Res.getHomeName());
        assertEquals(Country.URUGUAY, match3Res.getAwayName());
    }


    @Test
    public void shouldThrowExceptionWhenFinishingNotExistingMatch() throws Exception {
        //given
        LiveMatches liveMatches = new LiveMatches();

        createMatch(liveMatches, Country.POLAND, Country.EQUADOR);
        createMatch(liveMatches, Country.POLAND, Country.URUGUAY);

        MatchEvent finishMatchEvent1 = new MatchEvent(MatchEventType.MATCH_FINISHED, Country.GERMANY, Country.URUGUAY);

        //when
        Exception exception = assertThrows(Exception.class, () -> liveMatches.finishMatch(finishMatchEvent1));
        Match resMatch1 = liveMatches.getMatch(Country.POLAND, Country.EQUADOR);
        Match resMatch2 = liveMatches.getMatch(Country.POLAND, Country.URUGUAY);


        //then
        assertEquals("Match does not exists", exception.getMessage());
        assertEquals(Country.POLAND, resMatch1.getHomeName());
        assertEquals(Country.EQUADOR, resMatch1.getAwayName());
        assertEquals(Country.POLAND, resMatch2.getHomeName());
        assertEquals(Country.URUGUAY, resMatch2.getAwayName());
    }


    @Test
    public void shouldUpdateMatchHomeScore() throws Exception {
        //given
        LiveMatches liveMatches = new LiveMatches();

        createMatch(liveMatches, Country.POLAND, Country.URUGUAY);

        //when
        MatchEvent matchHomeScoreEvent = new MatchEvent(MatchEventType.HOME_TEAM_SCORES, Country.POLAND, Country.URUGUAY);
        liveMatches.updateMatchScore(matchHomeScoreEvent);

        //then
        Match match = liveMatches.getMatch(Country.POLAND, Country.URUGUAY);
        assertEquals(1, match.getHomeScore());
    }


    @Test
    public void shouldUpdateMatchHomeScoreTwice() throws Exception {
        //given
        LiveMatches liveMatches = new LiveMatches();

        createMatch(liveMatches, Country.POLAND, Country.URUGUAY);

        //when
        repeatScoreEvent(liveMatches, 2, MatchEventType.HOME_TEAM_SCORES, Country.POLAND, Country.URUGUAY);

        //then
        Match match = liveMatches.getMatch(Country.POLAND, Country.URUGUAY);
        assertEquals(2, match.getHomeScore());
    }


    @Test
    public void shouldUpdateMatchAwayScore() throws Exception {
        //given
        LiveMatches liveMatches = new LiveMatches();

        createMatch(liveMatches, Country.POLAND, Country.URUGUAY);

        //when
        MatchEvent matchAwayScoreEvent = new MatchEvent(MatchEventType.AWAY_TEAM_SCORES, Country.POLAND, Country.URUGUAY);
        liveMatches.updateMatchScore(matchAwayScoreEvent);

        //then
        Match match = liveMatches.getMatch(Country.POLAND, Country.URUGUAY);
        assertEquals(1, match.getAwayScore());
    }


    @Test
    public void shouldUpdateMatchAwayScoreTwice() throws Exception {
        //given
        LiveMatches liveMatches = new LiveMatches();

        createMatch(liveMatches, Country.POLAND, Country.URUGUAY);

        //when
        repeatScoreEvent(liveMatches, 2, MatchEventType.AWAY_TEAM_SCORES, Country.POLAND, Country.URUGUAY);


        //then
        Match match = liveMatches.getMatch(Country.POLAND, Country.URUGUAY);
        assertEquals(2, match.getAwayScore());
    }


    @Test
    public void shouldThrowExceptionWhenEventTypeMismatchOccursOnUpdateScore() {
        //given
        LiveMatches liveMatches = new LiveMatches();
        MatchEvent matchEvent = new MatchEvent(MatchEventType.MATCH_STARTED, Country.POLAND, Country.URUGUAY);

        //when
        Exception exception = assertThrows(Exception.class, () -> liveMatches.updateMatchScore(matchEvent));

        //then
        assertEquals("MatchEventType mismatch - expected: [HOME_TEAM_SCORES, AWAY_TEAM_SCORES] got: MATCH_STARTED", exception.getMessage());
    }


    @Test
    public void shouldOrderMatchesByHighestScoreSum() throws Exception {
        //given
        LiveMatches liveMatches = new LiveMatches();

        createMatch(liveMatches, Country.POLAND, Country.URUGUAY);
        createMatch(liveMatches, Country.MEXICO, Country.CANADA);
        createMatch(liveMatches, Country.GERMANY, Country.FRANCE);
        createMatch(liveMatches, Country.ARGENTINA, Country.AUSTRALIA);

        repeatScoreEvent(liveMatches, 3, MatchEventType.HOME_TEAM_SCORES, Country.MEXICO, Country.CANADA);

        repeatScoreEvent(liveMatches, 4, MatchEventType.AWAY_TEAM_SCORES, Country.GERMANY, Country.FRANCE);

        repeatScoreEvent(liveMatches, 3, MatchEventType.HOME_TEAM_SCORES, Country.ARGENTINA, Country.AUSTRALIA);
        repeatScoreEvent(liveMatches, 3, MatchEventType.AWAY_TEAM_SCORES, Country.ARGENTINA, Country.AUSTRALIA);


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
    public void shouldOrderMatchesByStartEvent() throws Exception {
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
    public void shouldOrderMatchesByScoreAndThenStartEvent() throws Exception {
        //given
        LiveMatches liveMatches = new LiveMatches();

        createMatch(liveMatches, Country.MEXICO, Country.CANADA);
        createMatch(liveMatches, Country.SPAIN, Country.BRAZIL);
        createMatch(liveMatches, Country.GERMANY, Country.FRANCE);
        createMatch(liveMatches, Country.URUGUAY, Country.ITALY);
        createMatch(liveMatches, Country.ARGENTINA, Country.AUSTRALIA);

        repeatScoreEvent(liveMatches, 5, MatchEventType.AWAY_TEAM_SCORES, Country.MEXICO, Country.CANADA);

        repeatScoreEvent(liveMatches, 10, MatchEventType.HOME_TEAM_SCORES, Country.SPAIN, Country.BRAZIL);
        repeatScoreEvent(liveMatches, 2, MatchEventType.AWAY_TEAM_SCORES, Country.SPAIN, Country.BRAZIL);

        repeatScoreEvent(liveMatches, 2, MatchEventType.HOME_TEAM_SCORES, Country.GERMANY, Country.FRANCE);
        repeatScoreEvent(liveMatches, 2, MatchEventType.AWAY_TEAM_SCORES, Country.GERMANY, Country.FRANCE);

        repeatScoreEvent(liveMatches, 6, MatchEventType.HOME_TEAM_SCORES, Country.URUGUAY, Country.ITALY);
        repeatScoreEvent(liveMatches, 6, MatchEventType.AWAY_TEAM_SCORES, Country.URUGUAY, Country.ITALY);

        repeatScoreEvent(liveMatches, 3, MatchEventType.HOME_TEAM_SCORES, Country.ARGENTINA, Country.AUSTRALIA);
        repeatScoreEvent(liveMatches, 1, MatchEventType.AWAY_TEAM_SCORES, Country.ARGENTINA, Country.AUSTRALIA);


        //when
        Match match1 = liveMatches.getCurrentMatches().getFirst();
        Match match2 = liveMatches.getCurrentMatches().get(1);
        Match match3 = liveMatches.getCurrentMatches().get(2);
        Match match4 = liveMatches.getCurrentMatches().get(3);
        Match match5 = liveMatches.getCurrentMatches().get(4);

        System.out.println(liveMatches.getCurrentMatches());
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


    private static void createMatch(LiveMatches liveMatches, String home, String away) throws Exception {
        MatchEvent matchEvent = new MatchEvent(MatchEventType.MATCH_STARTED, home, away);
        liveMatches.addMatch(matchEvent);
    }


    private static void finishMatch(LiveMatches liveMatches, String home, String away) throws Exception {
        MatchEvent matchEvent = new MatchEvent(MatchEventType.MATCH_FINISHED, home, away);
        liveMatches.finishMatch(matchEvent);
    }


    private static void repeatScoreEvent(LiveMatches liveMatches, int times, MatchEventType matchEventType, String home, String away) {
        IntStream.range(0, times).forEach(i -> {
            MatchEvent matchEvent = new MatchEvent(matchEventType, home, away);
            try {
                liveMatches.updateMatchScore(matchEvent);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        });
    }

}
