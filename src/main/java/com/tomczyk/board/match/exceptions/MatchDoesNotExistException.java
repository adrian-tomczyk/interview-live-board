package com.tomczyk.board.match.exceptions;

public class MatchDoesNotExistException extends RuntimeException {
    public MatchDoesNotExistException() {
        super("Match does not exist");
    }
}
