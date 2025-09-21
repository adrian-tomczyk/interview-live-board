import com.tomczyk.board.LiveFootballWorldCupScoreBoard;
import com.tomczyk.board.livematches.LiveMatches;
import com.tomczyk.board.match.Match;
import com.tomczyk.board.match.MatchEventType;
import com.tomczyk.board.utils.Country;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class LiveFootballWorldCupScoreBoardTest {

    LiveFootballWorldCupScoreBoard liveFootballWorldCupScoreBoard;

    @Mock
    LiveMatches liveMatches;


    @BeforeEach
    public void setup() {
        liveFootballWorldCupScoreBoard = new LiveFootballWorldCupScoreBoard(liveMatches);
    }


    @Test
    public void shouldStartAGame() throws Exception {
        //given

        //when
        liveFootballWorldCupScoreBoard.startGame(Country.POLAND, Country.URUGUAY);

        //then
        verify(liveMatches, times(1)).addMatch(argThat(matchEvent ->
                matchEvent.matchEventType() == MatchEventType.MATCH_STARTED &&
                        matchEvent.home().equals(Country.POLAND) &&
                        matchEvent.away().equals(Country.URUGUAY)));
    }


    @Test
    public void shouldFinishAGame() throws Exception {
        //given
        liveFootballWorldCupScoreBoard.startGame(Country.POLAND, Country.URUGUAY);

        //when
        liveFootballWorldCupScoreBoard.finishGame(Country.POLAND, Country.URUGUAY);

        //then
        verify(liveMatches, times(1)).finishMatch(argThat(matchEvent ->
                matchEvent.matchEventType() == MatchEventType.MATCH_FINISHED &&
                        matchEvent.home().equals(Country.POLAND) &&
                        matchEvent.away().equals(Country.URUGUAY)));
    }


    @Test
    public void shouldUpdateHomeScore() throws Exception {
        //given
        liveFootballWorldCupScoreBoard.startGame(Country.POLAND, Country.URUGUAY);

        //when
        liveFootballWorldCupScoreBoard.updateScore(Country.POLAND, Country.URUGUAY, MatchEventType.HOME_TEAM_SCORES);


        //then
        verify(liveMatches, times(1)).updateMatchScore(argThat(matchEvent ->
                matchEvent.matchEventType() == MatchEventType.HOME_TEAM_SCORES &&
                        matchEvent.home().equals(Country.POLAND) &&
                        matchEvent.away().equals(Country.URUGUAY)));
    }


    @Test
    public void shouldUpdateAwayScore() throws Exception {
        //given
        liveFootballWorldCupScoreBoard.startGame(Country.POLAND, Country.URUGUAY);

        //when
        liveFootballWorldCupScoreBoard.updateScore(Country.POLAND, Country.URUGUAY, MatchEventType.AWAY_TEAM_SCORES);


        //then
        verify(liveMatches, times(1)).updateMatchScore(argThat(matchEvent ->
                matchEvent.matchEventType() == MatchEventType.AWAY_TEAM_SCORES &&
                        matchEvent.home().equals(Country.POLAND) &&
                        matchEvent.away().equals(Country.URUGUAY)));
    }


    @Test
    public void shouldGetScoreBoardString() {
        //given
        Match match1 = new Match(Country.POLAND, Country.URUGUAY);
        match1.scoreHome();

        Match match2 = new Match(Country.MEXICO, Country.CANADA);
        match2.scoreAway();
        match2.scoreAway();

        //when
        when(liveMatches.getCurrentMatches())
                .thenReturn(List.of(match2,match1));

        String scoreBoard = liveFootballWorldCupScoreBoard.getScoreBoard();

        //then
        verify(liveMatches, times(1)).getCurrentMatches();
        assertEquals("""
                Mexico - Canada: 0-2
                Poland - Uruguay: 1-0""", scoreBoard);
    }

}
