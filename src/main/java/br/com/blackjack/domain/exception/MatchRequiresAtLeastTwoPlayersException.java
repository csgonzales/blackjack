package br.com.blackjack.domain.exception;

public class MatchRequiresAtLeastTwoPlayersException extends RuntimeException {

    public MatchRequiresAtLeastTwoPlayersException() {
        super("Match requires at least two players!");
    }
}
