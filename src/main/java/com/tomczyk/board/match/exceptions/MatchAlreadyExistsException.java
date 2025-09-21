package com.tomczyk.board.match.exceptions;

public class MatchAlreadyExistsException extends RuntimeException {
    public MatchAlreadyExistsException() {
        super("Match already exists");
    }
}
