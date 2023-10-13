package br.com.blackjack.domain.exception;

public class MatchNotStartedException extends RuntimeException {

    public MatchNotStartedException() {
        super("Match not started!");
    }
}
