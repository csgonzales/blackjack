package br.com.blackjack.domain.exception;

public class CardsNotShuffledException extends RuntimeException {

    public CardsNotShuffledException() {
        super("Cards not shuffled yet!");
    }

}
