package br.com.blackjack.domain.exception;

public class MatchNotEndedException extends RuntimeException {

    public MatchNotEndedException() {
        super("Match not ended!");
    }
}
