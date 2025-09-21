package com.tomczyk.board.match.exceptions;

public class MatchTeamsDoNotMatchException extends RuntimeException {
    public MatchTeamsDoNotMatchException() {
        super("Match teams do not match");
    }
}
