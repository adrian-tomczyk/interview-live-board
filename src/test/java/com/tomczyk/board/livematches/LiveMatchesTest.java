package com.tomczyk.board.livematches;

import com.tomczyk.board.match.Match;
import com.tomczyk.board.match.MatchEvent;
import com.tomczyk.board.match.MatchEventType;
import org.junit.jupiter.api.Test;

import java.util.stream.IntStream;

import static org.junit.jupiter.api.Assertions.*;

public class LiveMatchesTest {

    final String POLAND = "Poland";
    final String URUGUAY = "Uruguay";
    final String MEXICO = "Mexico";
    final String CANADA = "Canada";
    final String SPAIN = "Spain";
    final String BRAZIL = "Brazil";
    final String GERMANY = "Germany";
    final String FRANCE = "France";
    final String ITALY = "Italy";
    final String ARGENTINA = "Argentina";
    final String AUSTRALIA = "Australia";
    final String EQUADOR = "Equador";

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
        createMatch(liveMatches, POLAND, URUGUAY);

        //then
        assertEquals(1, liveMatches.getCurrentMatches().size());
    }


    @Test
    public void shouldThrowExceptionWhenMatchAlreadyExists() throws Exception {
        //given
        LiveMatches liveMatches = new LiveMatches();
        createMatch(liveMatches, POLAND, URUGUAY);


        //when
        Exception exception = assertThrows(Exception.class, () -> createMatch(liveMatches, POLAND, URUGUAY));

        //then
        assertEquals("Match already exists", exception.getMessage());
        assertEquals(1, liveMatches.getCurrentMatches().size());
    }


    @Test
    public void shouldThrowExceptionWhenEventTypeMismatchOccursDuringAddingMatch() {
        //given
        LiveMatches liveMatches = new LiveMatches();
        MatchEvent matchEvent = new MatchEvent(MatchEventType.MATCH_FINISHED, POLAND, URUGUAY);


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
        createMatch(liveMatches, POLAND, URUGUAY);
        createMatch(liveMatches, GERMANY, FRANCE);

        //then
        assertEquals(2, liveMatches.getCurrentMatches().size());
    }


    @Test
    public void shouldGetAddedMatch() throws Exception {
        //given
        LiveMatches liveMatches = new LiveMatches();
        createMatch(liveMatches, POLAND, URUGUAY);

        //when
        Match resMatch = liveMatches.getMatch(POLAND, URUGUAY);

        //then
        assertEquals(POLAND, resMatch.getHomeName());
        assertEquals(URUGUAY, resMatch.getAwayName());
    }


    @Test
    public void shouldNotGetAddedMatchWhenDoesNotExists() {
        //given
        LiveMatches liveMatches = new LiveMatches();

        //when
        Match resMatch = liveMatches.getMatch(POLAND, URUGUAY);

        //then
        assertNull(resMatch);
    }


    @Test
    public void shouldFinishMatch() throws Exception {
        //given
        LiveMatches liveMatches = new LiveMatches();
        createMatch(liveMatches, POLAND, URUGUAY);

        //when
        finishMatch(liveMatches, POLAND, URUGUAY);
        Match resMatch = liveMatches.getMatch(POLAND, URUGUAY);

        //then
        assertNull(resMatch);
    }


    @Test
    public void shouldThrowExceptionWhenEventTypeMismatchOccursDuringFinishingMatch() {
        //given
        LiveMatches liveMatches = new LiveMatches();
        MatchEvent matchEvent = new MatchEvent(MatchEventType.MATCH_STARTED, POLAND, URUGUAY);

        //when
        Exception exception = assertThrows(Exception.class, () -> liveMatches.finishMatch(matchEvent));

        //then
        assertEquals("MatchEventType mismatch - expected: [MATCH_FINISHED] got: MATCH_STARTED", exception.getMessage());
    }


    @Test
    public void shouldFinishOnlyMatchingMatch() throws Exception {
        //given
        LiveMatches liveMatches = new LiveMatches();
        createMatch(liveMatches, POLAND, EQUADOR);
        createMatch(liveMatches, POLAND, URUGUAY);
        createMatch(liveMatches, EQUADOR, URUGUAY);

        //when
        finishMatch(liveMatches, POLAND, URUGUAY);

        Match finishedMatch = liveMatches.getMatch(POLAND, URUGUAY);
        Match match1Res = liveMatches.getMatch(POLAND, EQUADOR);
        Match match3Res = liveMatches.getMatch(EQUADOR, URUGUAY);

        //then
        assertNull(finishedMatch);
        assertEquals(POLAND, match1Res.getHomeName());
        assertEquals(EQUADOR, match1Res.getAwayName());
        assertEquals(EQUADOR, match3Res.getHomeName());
        assertEquals(URUGUAY, match3Res.getAwayName());
    }


    @Test
    public void shouldThrowExceptionWhenFinishingNotExistingMatch() throws Exception {
        //given
        LiveMatches liveMatches = new LiveMatches();

        createMatch(liveMatches, POLAND, EQUADOR);
        createMatch(liveMatches, POLAND, URUGUAY);

        MatchEvent finishMatchEvent1 = new MatchEvent(MatchEventType.MATCH_FINISHED, GERMANY, URUGUAY);

        //when
        Exception exception = assertThrows(Exception.class, () -> liveMatches.finishMatch(finishMatchEvent1));
        Match resMatch1 = liveMatches.getMatch(POLAND, EQUADOR);
        Match resMatch2 = liveMatches.getMatch(POLAND, URUGUAY);


        //then
        assertEquals("Match does not exists", exception.getMessage());
        assertEquals(POLAND, resMatch1.getHomeName());
        assertEquals(EQUADOR, resMatch1.getAwayName());
        assertEquals(POLAND, resMatch2.getHomeName());
        assertEquals(URUGUAY, resMatch2.getAwayName());
    }


    @Test
    public void shouldUpdateMatchHomeScore() throws Exception {
        //given
        LiveMatches liveMatches = new LiveMatches();

        createMatch(liveMatches, POLAND, URUGUAY);

        //when
        MatchEvent matchHomeScoreEvent = new MatchEvent(MatchEventType.HOME_TEAM_SCORES, POLAND, URUGUAY);
        liveMatches.updateMatchScore(matchHomeScoreEvent);

        //then
        Match match = liveMatches.getMatch(POLAND, URUGUAY);
        assertEquals(1, match.getHomeScore());
    }


    @Test
    public void shouldUpdateMatchHomeScoreTwice() throws Exception {
        //given
        LiveMatches liveMatches = new LiveMatches();

        createMatch(liveMatches, POLAND, URUGUAY);

        //when
        repeatScoreEvent(liveMatches, 2, MatchEventType.HOME_TEAM_SCORES, POLAND, URUGUAY);

        //then
        Match match = liveMatches.getMatch(POLAND, URUGUAY);
        assertEquals(2, match.getHomeScore());
    }


    @Test
    public void shouldUpdateMatchAwayScore() throws Exception {
        //given
        LiveMatches liveMatches = new LiveMatches();

        createMatch(liveMatches, POLAND, URUGUAY);

        //when
        MatchEvent matchAwayScoreEvent = new MatchEvent(MatchEventType.AWAY_TEAM_SCORES, POLAND, URUGUAY);
        liveMatches.updateMatchScore(matchAwayScoreEvent);

        //then
        Match match = liveMatches.getMatch(POLAND, URUGUAY);
        assertEquals(1, match.getAwayScore());
    }


    @Test
    public void shouldUpdateMatchAwayScoreTwice() throws Exception {
        //given
        LiveMatches liveMatches = new LiveMatches();

        createMatch(liveMatches, POLAND, URUGUAY);

        //when
        repeatScoreEvent(liveMatches, 2, MatchEventType.AWAY_TEAM_SCORES, POLAND, URUGUAY);


        //then
        Match match = liveMatches.getMatch(POLAND, URUGUAY);
        assertEquals(2, match.getAwayScore());
    }


    @Test
    public void shouldThrowExceptionWhenEventTypeMismatchOccursOnUpdateScore() {
        //given
        LiveMatches liveMatches = new LiveMatches();
        MatchEvent matchEvent = new MatchEvent(MatchEventType.MATCH_STARTED, POLAND, URUGUAY);

        //when
        Exception exception = assertThrows(Exception.class, () -> liveMatches.updateMatchScore(matchEvent));

        //then
        assertEquals("MatchEventType mismatch - expected: [HOME_TEAM_SCORES, AWAY_TEAM_SCORES] got: MATCH_STARTED", exception.getMessage());
    }


    @Test
    public void shouldOrderMatchesByHighestScoreSum() throws Exception {
        //given
        LiveMatches liveMatches = new LiveMatches();

        createMatch(liveMatches, POLAND, URUGUAY);
        createMatch(liveMatches, MEXICO, CANADA);
        createMatch(liveMatches, GERMANY, FRANCE);
        createMatch(liveMatches, ARGENTINA, AUSTRALIA);

        repeatScoreEvent(liveMatches, 3, MatchEventType.HOME_TEAM_SCORES, MEXICO, CANADA);

        repeatScoreEvent(liveMatches, 4, MatchEventType.AWAY_TEAM_SCORES, GERMANY, FRANCE);

        repeatScoreEvent(liveMatches, 3, MatchEventType.HOME_TEAM_SCORES, ARGENTINA, AUSTRALIA);
        repeatScoreEvent(liveMatches, 3, MatchEventType.AWAY_TEAM_SCORES, ARGENTINA, AUSTRALIA);


        //when
        Match match1 = liveMatches.getCurrentMatches().getFirst();
        Match match2 = liveMatches.getCurrentMatches().get(1);
        Match match3 = liveMatches.getCurrentMatches().get(2);
        Match match4 = liveMatches.getCurrentMatches().get(3);


        //then
        assertEquals(ARGENTINA, match1.getHomeName());
        assertEquals(AUSTRALIA, match1.getAwayName());

        assertEquals(GERMANY, match2.getHomeName());
        assertEquals(FRANCE, match2.getAwayName());

        assertEquals(MEXICO, match3.getHomeName());
        assertEquals(CANADA, match3.getAwayName());

        assertEquals(POLAND, match4.getHomeName());
        assertEquals(URUGUAY, match4.getAwayName());
    }


    @Test
    public void shouldOrderMatchesByStartEvent() throws Exception {
        //given
        LiveMatches liveMatches = new LiveMatches();

        createMatch(liveMatches, POLAND, URUGUAY);
        createMatch(liveMatches, MEXICO, CANADA);
        createMatch(liveMatches, GERMANY, FRANCE);
        createMatch(liveMatches, ARGENTINA, AUSTRALIA);

        //when
        Match match1 = liveMatches.getCurrentMatches().getFirst();
        Match match2 = liveMatches.getCurrentMatches().get(1);
        Match match3 = liveMatches.getCurrentMatches().get(2);
        Match match4 = liveMatches.getCurrentMatches().get(3);


        //then
        assertEquals(POLAND, match1.getHomeName());
        assertEquals(URUGUAY, match1.getAwayName());

        assertEquals(MEXICO, match2.getHomeName());
        assertEquals(CANADA, match2.getAwayName());

        assertEquals(GERMANY, match3.getHomeName());
        assertEquals(FRANCE, match3.getAwayName());

        assertEquals(ARGENTINA, match4.getHomeName());
        assertEquals(AUSTRALIA, match4.getAwayName());
    }


    @Test
    public void shouldOrderMatchesByScoreAndThenStartEvent() throws Exception {
        //given
        LiveMatches liveMatches = new LiveMatches();

        createMatch(liveMatches, MEXICO, CANADA);
        createMatch(liveMatches, SPAIN, BRAZIL);
        createMatch(liveMatches, GERMANY, FRANCE);
        createMatch(liveMatches, URUGUAY, ITALY);
        createMatch(liveMatches, ARGENTINA, AUSTRALIA);

        repeatScoreEvent(liveMatches, 5, MatchEventType.AWAY_TEAM_SCORES, MEXICO, CANADA);

        repeatScoreEvent(liveMatches, 10, MatchEventType.HOME_TEAM_SCORES, SPAIN, BRAZIL);
        repeatScoreEvent(liveMatches, 2, MatchEventType.AWAY_TEAM_SCORES, SPAIN, BRAZIL);

        repeatScoreEvent(liveMatches, 2, MatchEventType.HOME_TEAM_SCORES, GERMANY, FRANCE);
        repeatScoreEvent(liveMatches, 2, MatchEventType.AWAY_TEAM_SCORES, GERMANY, FRANCE);

        repeatScoreEvent(liveMatches, 6, MatchEventType.HOME_TEAM_SCORES, URUGUAY, ITALY);
        repeatScoreEvent(liveMatches, 6, MatchEventType.AWAY_TEAM_SCORES, URUGUAY, ITALY);

        repeatScoreEvent(liveMatches, 3, MatchEventType.HOME_TEAM_SCORES, ARGENTINA, AUSTRALIA);
        repeatScoreEvent(liveMatches, 1, MatchEventType.AWAY_TEAM_SCORES, ARGENTINA, AUSTRALIA);


        //when
        Match match1 = liveMatches.getCurrentMatches().getFirst();
        Match match2 = liveMatches.getCurrentMatches().get(1);
        Match match3 = liveMatches.getCurrentMatches().get(2);
        Match match4 = liveMatches.getCurrentMatches().get(3);
        Match match5 = liveMatches.getCurrentMatches().get(4);

        System.out.println(liveMatches.getCurrentMatches());
        //then
        assertEquals(URUGUAY, match1.getHomeName());
        assertEquals(ITALY, match1.getAwayName());

        assertEquals(SPAIN, match2.getHomeName());
        assertEquals(BRAZIL, match2.getAwayName());

        assertEquals(MEXICO, match3.getHomeName());
        assertEquals(CANADA, match3.getAwayName());

        assertEquals(ARGENTINA, match4.getHomeName());
        assertEquals(AUSTRALIA, match4.getAwayName());

        assertEquals(GERMANY, match5.getHomeName());
        assertEquals(FRANCE, match5.getAwayName());
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
