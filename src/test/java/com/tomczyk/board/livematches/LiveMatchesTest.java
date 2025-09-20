package com.tomczyk.board.livematches;

import com.tomczyk.board.match.Match;
import com.tomczyk.board.match.MatchEvent;
import com.tomczyk.board.match.MatchEventType;
import org.junit.jupiter.api.Test;

import java.util.stream.IntStream;

import static org.junit.jupiter.api.Assertions.*;

public class LiveMatchesTest {

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
        MatchEvent matchEvent = new MatchEvent(MatchEventType.MATCH_STARTED, "Poland", "Uruguay");


        //when
        liveMatches.addMatch(matchEvent);

        //then
        assertEquals(1, liveMatches.getCurrentMatches().size());
    }


    @Test
    public void shouldThrowExceptionWhenMatchAlreadyExists() throws Exception {
        //given
        LiveMatches liveMatches = new LiveMatches();
        MatchEvent matchEvent = new MatchEvent(MatchEventType.MATCH_STARTED, "Poland", "Uruguay");
        liveMatches.addMatch(matchEvent);

        //when
        Exception exception = assertThrows(Exception.class, ()-> liveMatches.addMatch(matchEvent));

        //then
        assertEquals("Match already exists", exception.getMessage());
        assertEquals(1, liveMatches.getCurrentMatches().size());
    }

    @Test
    public void shouldThrowExceptionWhenEventTypeMismatchOccursDuringAddingMatch() {
        //given
        LiveMatches liveMatches = new LiveMatches();
        MatchEvent matchEvent = new MatchEvent(MatchEventType.MATCH_FINISHED, "Poland", "Uruguay");


        //when
        Exception exception = assertThrows(Exception.class, () -> liveMatches.addMatch(matchEvent));

        //then
        assertEquals("MatchEventType mismatch - expected: [MATCH_STARTED] got: MATCH_FINISHED", exception.getMessage());
    }

    @Test
    public void shouldAddMultipleMatchesToList() throws Exception {
        //given
        LiveMatches liveMatches = new LiveMatches();
        MatchEvent matchEvent1 = new MatchEvent(MatchEventType.MATCH_STARTED, "Poland", "Uruguay");
        MatchEvent matchEvent2 = new MatchEvent(MatchEventType.MATCH_STARTED, "Germany", "France");

        //when
        liveMatches.addMatch(matchEvent1);
        liveMatches.addMatch(matchEvent2);

        //then
        assertEquals(2, liveMatches.getCurrentMatches().size());
    }

    @Test
    public void shouldGetAddedMatch() throws Exception {
        //given
        LiveMatches liveMatches = new LiveMatches();
        MatchEvent matchEvent1 = new MatchEvent(MatchEventType.MATCH_STARTED, "Poland", "Uruguay");
        liveMatches.addMatch(matchEvent1);

        //when
        Match resMatch = liveMatches.getMatch("Poland", "Uruguay");

        //then
        assertEquals("Poland", resMatch.getHomeName());
        assertEquals("Uruguay", resMatch.getAwayName());
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
        MatchEvent matchEvent1 = new MatchEvent(MatchEventType.MATCH_STARTED, "Poland", "Uruguay");
        MatchEvent finishMatchEvent1 = new MatchEvent(MatchEventType.MATCH_FINISHED, "Poland", "Uruguay");
        liveMatches.addMatch(matchEvent1);

        //when
        liveMatches.finishMatch(finishMatchEvent1);
        Match resMatch = liveMatches.getMatch("Poland", "Uruguay");

        //then
        assertNull(resMatch);
    }

    @Test
    public void shouldThrowExceptionWhenEventTypeMismatchOccursDuringFinishingMatch() {
        //given
        LiveMatches liveMatches = new LiveMatches();
        MatchEvent matchEvent = new MatchEvent(MatchEventType.MATCH_STARTED, "Poland", "Uruguay");


        //when
        Exception exception = assertThrows(Exception.class, () -> liveMatches.finishMatch(matchEvent));

        //then
        assertEquals("MatchEventType mismatch - expected: [MATCH_FINISHED] got: MATCH_STARTED", exception.getMessage());
    }

    @Test
    public void shouldFinishOnlyMatchingMatch() throws Exception {
        //given
        LiveMatches liveMatches = new LiveMatches();
        MatchEvent matchEvent1 = new MatchEvent(MatchEventType.MATCH_STARTED, "Poland", "Equador");
        MatchEvent matchEvent2 = new MatchEvent(MatchEventType.MATCH_STARTED, "Poland", "Uruguay");
        MatchEvent matchEvent3 = new MatchEvent(MatchEventType.MATCH_STARTED, "Equador", "Uruguay");
        MatchEvent finishMatchEvent1 = new MatchEvent(MatchEventType.MATCH_FINISHED, "Poland", "Uruguay");

        liveMatches.addMatch(matchEvent1);
        liveMatches.addMatch(matchEvent2);
        liveMatches.addMatch(matchEvent3);

        //when
        liveMatches.finishMatch(finishMatchEvent1);
        Match finishedMatch = liveMatches.getMatch("Poland", "Uruguay");
        Match match1Res = liveMatches.getMatch("Poland", "Equador");
        Match match3Res = liveMatches.getMatch("Equador", "Uruguay");

        //then
        assertNull(finishedMatch);
        assertEquals("Poland", match1Res.getHomeName());
        assertEquals("Equador", match1Res.getAwayName());
        assertEquals("Equador", match3Res.getHomeName());
        assertEquals("Uruguay", match3Res.getAwayName());
    }

    @Test
    public void shouldThrowExceptionWhenFinishingNotExistingMatch() throws Exception {
        //given
        LiveMatches liveMatches = new LiveMatches();

        MatchEvent matchEvent1 = new MatchEvent(MatchEventType.MATCH_STARTED, "Poland", "Equador");
        MatchEvent matchEvent2 = new MatchEvent(MatchEventType.MATCH_STARTED, "Poland", "Uruguay");
        MatchEvent finishMatchEvent1 = new MatchEvent(MatchEventType.MATCH_FINISHED, "Germany", "Uruguay");


        liveMatches.addMatch(matchEvent1);
        liveMatches.addMatch(matchEvent2);

        //when
        Exception exception = assertThrows(Exception.class, () -> liveMatches.finishMatch(finishMatchEvent1));
        Match resMatch1 = liveMatches.getMatch("Poland", "Equador");
        Match resMatch2 = liveMatches.getMatch("Poland", "Uruguay");


        //then
        assertEquals("Match does not exists", exception.getMessage());
        assertEquals("Poland", resMatch1.getHomeName());
        assertEquals("Equador", resMatch1.getAwayName());
        assertEquals("Poland", resMatch2.getHomeName());
        assertEquals("Uruguay", resMatch2.getAwayName());
    }

    @Test
    public void shouldUpdateMatchHomeScore() throws Exception {
        //given
        LiveMatches liveMatches = new LiveMatches();

        MatchEvent matchStartEvent = new MatchEvent(MatchEventType.MATCH_STARTED, "Poland", "Uruguay");
        liveMatches.addMatch(matchStartEvent);

        //when
        MatchEvent matchHomeScoreEvent = new MatchEvent(MatchEventType.HOME_TEAM_SCORES, "Poland", "Uruguay");
        liveMatches.updateMatchScore(matchHomeScoreEvent);

        //then
        Match match = liveMatches.getMatch("Poland", "Uruguay");
        assertEquals(1, match.getHomeScore());
    }

    @Test
    public void shouldUpdateMatchHomeScoreTwice() throws Exception {
        //given
        LiveMatches liveMatches = new LiveMatches();

        MatchEvent matchStartEvent = new MatchEvent(MatchEventType.MATCH_STARTED, "Poland", "Uruguay");
        liveMatches.addMatch(matchStartEvent);

        //when
        MatchEvent matchHomeScoreEvent = new MatchEvent(MatchEventType.HOME_TEAM_SCORES, "Poland", "Uruguay");
        liveMatches.updateMatchScore(matchHomeScoreEvent);
        liveMatches.updateMatchScore(matchHomeScoreEvent);

        //then
        Match match = liveMatches.getMatch("Poland", "Uruguay");
        assertEquals(2, match.getHomeScore());
    }

    @Test
    public void shouldUpdateMatchAwayScore() throws Exception {
        //given
        LiveMatches liveMatches = new LiveMatches();

        MatchEvent matchStartEvent = new MatchEvent(MatchEventType.MATCH_STARTED, "Poland", "Uruguay");
        liveMatches.addMatch(matchStartEvent);

        //when
        MatchEvent matchAwayScoreEvent = new MatchEvent(MatchEventType.AWAY_TEAM_SCORES, "Poland", "Uruguay");
        liveMatches.updateMatchScore(matchAwayScoreEvent);

        //then
        Match match = liveMatches.getMatch("Poland", "Uruguay");
        assertEquals(1, match.getAwayScore());
    }

    @Test
    public void shouldUpdateMatchAwayScoreTwice() throws Exception {
        //given
        LiveMatches liveMatches = new LiveMatches();

        MatchEvent matchStartEvent = new MatchEvent(MatchEventType.MATCH_STARTED, "Poland", "Uruguay");
        liveMatches.addMatch(matchStartEvent);

        //when
        MatchEvent matchAwayScoreEvent = new MatchEvent(MatchEventType.AWAY_TEAM_SCORES, "Poland", "Uruguay");
        liveMatches.updateMatchScore(matchAwayScoreEvent);
        liveMatches.updateMatchScore(matchAwayScoreEvent);

        //then
        Match match = liveMatches.getMatch("Poland", "Uruguay");
        assertEquals(2, match.getAwayScore());
    }

    @Test
    public void shouldThrowExceptionWhenEventTypeMismatchOccursOnUpdateScore() {
        //given
        LiveMatches liveMatches = new LiveMatches();
        MatchEvent matchEvent = new MatchEvent(MatchEventType.MATCH_STARTED, "Poland", "Uruguay");

        //when
        Exception exception = assertThrows(Exception.class, () -> liveMatches.updateMatchScore(matchEvent));

        //then
        assertEquals("MatchEventType mismatch - expected: [HOME_TEAM_SCORES, AWAY_TEAM_SCORES] got: MATCH_STARTED", exception.getMessage());
    }

    @Test
    public void shouldOrderMatchesByHighestScoreSum() throws Exception {
        //given
        LiveMatches liveMatches = new LiveMatches();

        MatchEvent matchStartEvent1 = new MatchEvent(MatchEventType.MATCH_STARTED, "Poland", "Uruguay");
        MatchEvent matchStartEvent2 = new MatchEvent(MatchEventType.MATCH_STARTED, "Mexico", "Canada");
        MatchEvent matchStartEvent3 = new MatchEvent(MatchEventType.MATCH_STARTED, "Germany", "France");
        MatchEvent matchStartEvent4 = new MatchEvent(MatchEventType.MATCH_STARTED, "Argentina", "Australia");

        liveMatches.addMatch(matchStartEvent1);
        liveMatches.addMatch(matchStartEvent2);
        liveMatches.addMatch(matchStartEvent3);
        liveMatches.addMatch(matchStartEvent4);

        MatchEvent matchHomeScoreEvent2 = new MatchEvent(MatchEventType.HOME_TEAM_SCORES, "Mexico", "Canada");
        MatchEvent matchHomeScoreEvent4 = new MatchEvent(MatchEventType.HOME_TEAM_SCORES, "Argentina", "Australia");
        MatchEvent matchAwayScoreEvent3 = new MatchEvent(MatchEventType.AWAY_TEAM_SCORES, "Germany", "France");
        MatchEvent matchAwayScoreEvent4 = new MatchEvent(MatchEventType.AWAY_TEAM_SCORES, "Argentina", "Australia");

        liveMatches.updateMatchScore(matchHomeScoreEvent2);
        liveMatches.updateMatchScore(matchHomeScoreEvent2);
        liveMatches.updateMatchScore(matchHomeScoreEvent2);

        liveMatches.updateMatchScore(matchAwayScoreEvent3);
        liveMatches.updateMatchScore(matchAwayScoreEvent3);
        liveMatches.updateMatchScore(matchAwayScoreEvent3);
        liveMatches.updateMatchScore(matchAwayScoreEvent3);

        liveMatches.updateMatchScore(matchHomeScoreEvent4);
        liveMatches.updateMatchScore(matchHomeScoreEvent4);
        liveMatches.updateMatchScore(matchHomeScoreEvent4);
        liveMatches.updateMatchScore(matchAwayScoreEvent4);
        liveMatches.updateMatchScore(matchAwayScoreEvent4);
        liveMatches.updateMatchScore(matchAwayScoreEvent4);

        //when
        Match match1 = liveMatches.getCurrentMatches().getFirst();
        Match match2 = liveMatches.getCurrentMatches().get(1);
        Match match3 = liveMatches.getCurrentMatches().get(2);
        Match match4 = liveMatches.getCurrentMatches().get(3);


        //then
        assertEquals("Argentina", match1.getHomeName());
        assertEquals("Australia", match1.getAwayName());

        assertEquals("Germany", match2.getHomeName());
        assertEquals("France", match2.getAwayName());

        assertEquals("Mexico", match3.getHomeName());
        assertEquals("Canada", match3.getAwayName());

        assertEquals("Poland", match4.getHomeName());
        assertEquals("Uruguay", match4.getAwayName());
    }

    @Test
    public void shouldOrderMatchesByStartEvent() throws Exception {
        //given
        LiveMatches liveMatches = new LiveMatches();

        MatchEvent matchStartEvent1 = new MatchEvent(MatchEventType.MATCH_STARTED, "Poland", "Uruguay");
        MatchEvent matchStartEvent2 = new MatchEvent(MatchEventType.MATCH_STARTED, "Mexico", "Canada");
        MatchEvent matchStartEvent3 = new MatchEvent(MatchEventType.MATCH_STARTED, "Germany", "France");
        MatchEvent matchStartEvent4 = new MatchEvent(MatchEventType.MATCH_STARTED, "Argentina", "Australia");

        liveMatches.addMatch(matchStartEvent1);
        liveMatches.addMatch(matchStartEvent2);
        liveMatches.addMatch(matchStartEvent3);
        liveMatches.addMatch(matchStartEvent4);

        //when
        Match match1 = liveMatches.getCurrentMatches().getFirst();
        Match match2 = liveMatches.getCurrentMatches().get(1);
        Match match3 = liveMatches.getCurrentMatches().get(2);
        Match match4 = liveMatches.getCurrentMatches().get(3);


        //then
        assertEquals("Poland", match1.getHomeName());
        assertEquals("Uruguay", match1.getAwayName());

        assertEquals("Mexico", match2.getHomeName());
        assertEquals("Canada", match2.getAwayName());

        assertEquals("Germany", match3.getHomeName());
        assertEquals("France", match3.getAwayName());

        assertEquals("Argentina", match4.getHomeName());
        assertEquals("Australia", match4.getAwayName());
    }


    @Test
    public void shouldOrderMatchesByScoreAndThenStartEvent() throws Exception {
        //given
        LiveMatches liveMatches = new LiveMatches();

        MatchEvent matchStartEvent1 = new MatchEvent(MatchEventType.MATCH_STARTED, "Mexico", "Canada");
        MatchEvent matchStartEvent2 = new MatchEvent(MatchEventType.MATCH_STARTED, "Spain", "Brazil");
        MatchEvent matchStartEvent3 = new MatchEvent(MatchEventType.MATCH_STARTED, "Germany", "France");
        MatchEvent matchStartEvent4 = new MatchEvent(MatchEventType.MATCH_STARTED, "Uruguay", "Italy");
        MatchEvent matchStartEvent5 = new MatchEvent(MatchEventType.MATCH_STARTED, "Argentina", "Australia");

        liveMatches.addMatch(matchStartEvent1);
        liveMatches.addMatch(matchStartEvent2);
        liveMatches.addMatch(matchStartEvent3);
        liveMatches.addMatch(matchStartEvent4);
        liveMatches.addMatch(matchStartEvent5);

        repeatScoreEvent(liveMatches, 5, MatchEventType.AWAY_TEAM_SCORES, "Mexico","Canada");

        repeatScoreEvent(liveMatches, 10, MatchEventType.HOME_TEAM_SCORES, "Spain","Brazil");
        repeatScoreEvent(liveMatches, 2, MatchEventType.AWAY_TEAM_SCORES, "Spain","Brazil");

        repeatScoreEvent(liveMatches, 2, MatchEventType.HOME_TEAM_SCORES, "Germany","France");
        repeatScoreEvent(liveMatches, 2, MatchEventType.AWAY_TEAM_SCORES, "Germany","France");

        repeatScoreEvent(liveMatches, 6, MatchEventType.HOME_TEAM_SCORES, "Uruguay","Italy");
        repeatScoreEvent(liveMatches, 6, MatchEventType.AWAY_TEAM_SCORES, "Uruguay","Italy");

        repeatScoreEvent(liveMatches, 3, MatchEventType.HOME_TEAM_SCORES, "Argentina","Australia");
        repeatScoreEvent(liveMatches, 1, MatchEventType.AWAY_TEAM_SCORES, "Argentina","Australia");


        //when
        Match match1 = liveMatches.getCurrentMatches().getFirst();
        Match match2 = liveMatches.getCurrentMatches().get(1);
        Match match3 = liveMatches.getCurrentMatches().get(2);
        Match match4 = liveMatches.getCurrentMatches().get(3);
        Match match5 = liveMatches.getCurrentMatches().get(4);

        System.out.println(liveMatches.getCurrentMatches());
        //then
        assertEquals("Uruguay", match1.getHomeName());
        assertEquals("Italy", match1.getAwayName());

        assertEquals("Spain", match2.getHomeName());
        assertEquals("Brazil", match2.getAwayName());

        assertEquals("Mexico", match3.getHomeName());
        assertEquals("Canada", match3.getAwayName());

        assertEquals("Argentina", match4.getHomeName());
        assertEquals("Australia", match4.getAwayName());

        assertEquals("Germany", match5.getHomeName());
        assertEquals("France", match5.getAwayName());
    }


}
