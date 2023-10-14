package br.com.blackjack.domain;

public interface Deck {

    void shuffle();

    Card draw();

    static Deck newDeck() {
        return new DeckImpl();
    }
}
