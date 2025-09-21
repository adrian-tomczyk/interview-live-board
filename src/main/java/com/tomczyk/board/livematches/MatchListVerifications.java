package com.tomczyk.board.livematches;

import com.tomczyk.board.match.Match;
import com.tomczyk.board.match.exceptions.MatchAlreadyExistsException;
import com.tomczyk.board.match.exceptions.MatchDoesNotExistException;

public class MatchListVerifications {
    static void throwIfMatchDoesNotExist(Match match) {
        if (match == null) {
            throw new MatchDoesNotExistException();
        }
    }


    static void throwIfMatchAlreadyExists(Match existingMatch) {
        if (existingMatch != null) {
            throw new MatchAlreadyExistsException();
        }
    }
}