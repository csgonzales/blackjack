package br.com.blackjack.domain.exception;

public class PlayersExceedLimitException extends RuntimeException {

    public PlayersExceedLimitException(int playersLimit) {
        super(String.format("Players exceed limit of %d!", playersLimit));
    }
}
