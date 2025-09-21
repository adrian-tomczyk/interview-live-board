import com.tomczyk.board.LiveFootballWorldCupScoreBoard;
import com.tomczyk.board.livematches.LiveMatches;
import com.tomczyk.board.match.Match;
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
    public void shouldStartAGame() {
        //given

        //when
        liveFootballWorldCupScoreBoard.startGame(Country.POLAND, Country.URUGUAY);

        //then
        verify(liveMatches, times(1)).addMatch(argThat(match ->
                Country.POLAND.equals(match.getHomeName()) && Country.URUGUAY.equals(match.getAwayName())
        ));
    }


    @Test
    public void shouldFinishAGame() {
        //given
        liveFootballWorldCupScoreBoard.startGame(Country.POLAND, Country.URUGUAY);

        //when
        liveFootballWorldCupScoreBoard.finishGame(Country.POLAND, Country.URUGUAY);

        //then
        verify(liveMatches, times(1)).finishMatch(
                argThat((home) -> Country.POLAND.equals(home)),
                argThat((away) -> Country.URUGUAY.equals(away))
        );
    }


    @Test
    public void shouldUpdateScore() {
        //given
        liveFootballWorldCupScoreBoard.startGame(Country.POLAND, Country.URUGUAY);

        //when
        liveFootballWorldCupScoreBoard.updateScore(Country.POLAND, Country.URUGUAY, 1, 2);


        //then
        verify(liveMatches, times(1)).updateScore(
                argThat((home) -> Country.POLAND.equals(home)),
                argThat((away) -> Country.URUGUAY.equals(away)),
                argThat((scoreHome) -> scoreHome == 1),
                argThat((scoreAway) -> scoreAway == 2)
        );
    }


    @Test
    public void shouldGetScoreBoardString() {
        //given
        Match match1 = new Match(Country.POLAND, Country.URUGUAY);
        match1.updateScore(Country.POLAND, Country.URUGUAY, 1, 0);

        Match match2 = new Match(Country.MEXICO, Country.CANADA);
        match1.updateScore(Country.POLAND, Country.URUGUAY, 1, 2);


        //when
        when(liveMatches.getCurrentMatches())
                .thenReturn(List.of(match2, match1));

        String scoreBoard = liveFootballWorldCupScoreBoard.getScoreBoard();

        //then
        verify(liveMatches, times(1)).getCurrentMatches();
        assertEquals("""
                Mexico - Canada: 0-2
                Poland - Uruguay: 1-0""", scoreBoard);
    }

}
