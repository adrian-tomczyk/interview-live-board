package com.tomczyk.board.match;

public record MatchEvent(MatchEventType matchEventType, String home, String away) {
}
