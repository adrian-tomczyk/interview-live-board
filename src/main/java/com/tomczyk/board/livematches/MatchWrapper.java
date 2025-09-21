package com.tomczyk.board.livematches;

import com.tomczyk.board.match.Match;

public record MatchWrapper(Match match, Integer order) {
}
