package com.tomczyk.board.match.exceptions;

public class UpdateScoreNegativeValueException extends RuntimeException {
    public UpdateScoreNegativeValueException() {
        super("Negative value in update score function occurred");
    }
}
