package com.tomczyk.board.livematches;

import com.tomczyk.board.match.Match;

public record OrderedMatchAdapter(Match match, Integer order) {
}
