import com.tomczyk.board.LiveFootballWorldCupScoreBoard;
import com.tomczyk.board.livematches.LiveMatches;
import com.tomczyk.board.match.MatchEventType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Spy;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

public class LiveFootballWorldCupScoreBoardTest {
    final String POLAND = "Poland";
    final String URUGUAY = "Uruguay";
    final String MEXICO = "Mexico";
    final String CANADA = "Canada";

    LiveFootballWorldCupScoreBoard liveFootballWorldCupScoreBoard;

    @Mock
    @Spy
    LiveMatches liveMatches;


    @BeforeEach
    public void setup() {
        liveFootballWorldCupScoreBoard = new LiveFootballWorldCupScoreBoard(liveMatches);
    }


    @Test
    public void shouldStartAGame() throws Exception {
        //given

        //when
        liveFootballWorldCupScoreBoard.startGame(POLAND, URUGUAY);

        //then
        verify(liveMatches, times(1)).addMatch(argThat(matchEvent ->
                matchEvent.matchEventType() == MatchEventType.MATCH_STARTED &&
                        matchEvent.home().equals(POLAND) &&
                        matchEvent.away().equals(URUGUAY)));
    }


    @Test
    public void shouldFinishAGame() throws Exception {
        //given
        liveFootballWorldCupScoreBoard.startGame(POLAND, URUGUAY);

        //when
        liveFootballWorldCupScoreBoard.finishGame(POLAND, URUGUAY);

        //then
        verify(liveMatches, times(1)).finishGame(argThat(matchEvent ->
                matchEvent.matchEventType() == MatchEventType.MATCH_FINISHED &&
                        matchEvent.home().equals(POLAND) &&
                        matchEvent.away().equals(URUGUAY)));
    }


    @Test
    public void shouldUpdateHomeScore() throws Exception {
        //given
        liveFootballWorldCupScoreBoard.startGame(POLAND, URUGUAY);

        //when
        liveFootballWorldCupScoreBoard.updateScore(POLAND, URUGUAY, MatchEventType.HOME_TEAM_SCORES);


        //then
        verify(liveMatches, times(1)).updateScore(argThat(matchEvent ->
                matchEvent.matchEventType() == MatchEventType.HOME_TEAM_SCORES &&
                        matchEvent.home().equals(POLAND) &&
                        matchEvent.away().equals(URUGUAY)));
    }


    @Test
    public void shouldUpdateHomeScore() throws Exception {
        //given
        liveFootballWorldCupScoreBoard.startGame(POLAND, URUGUAY);

        //when
        liveFootballWorldCupScoreBoard.updateScore(POLAND, URUGUAY, MatchEventType.AWAY_TEAM_SCORES);


        //then
        verify(liveMatches, times(1)).updateScore(argThat(matchEvent ->
                matchEvent.matchEventType() == MatchEventType.AWAY_TEAM_SCORES &&
                        matchEvent.home().equals(POLAND) &&
                        matchEvent.away().equals(URUGUAY)));
    }


    @Test
    public void shouldGetScoreBoardString() throws Exception {
        //given
        liveFootballWorldCupScoreBoard.startGame(POLAND, URUGUAY);
        liveFootballWorldCupScoreBoard.startGame(MEXICO, CANADA);

        liveFootballWorldCupScoreBoard.updateScore(POLAND, URUGUAY, MatchEventType.HOME_TEAM_SCORES);
        liveFootballWorldCupScoreBoard.updateScore(MEXICO, CANADA, MatchEventType.AWAY_TEAM_SCORES);
        liveFootballWorldCupScoreBoard.updateScore(MEXICO, CANADA, MatchEventType.AWAY_TEAM_SCORES);

        //when
        String scoreBoard = liveFootballWorldCupScoreBoard.getScoreBoard();

        //then
        verify(liveMatches, times(1)).getCurrentMatches();
        assertEquals("""
                Mexico - Canada: 0-2
                Poland - Uruguay: 1-0""", scoreBoard);
    }

}
