package br.com.blackjack.domain.exception;

public class NoCardsLeftException extends RuntimeException {

    public NoCardsLeftException() {
        super("No cards left!");
    }
}
