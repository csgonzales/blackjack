package br.com.blackjack.domain.exception;

public class MatchAlreadyEndedException extends RuntimeException {

    public MatchAlreadyEndedException() {
        super("Match already ended!");
    }
}
