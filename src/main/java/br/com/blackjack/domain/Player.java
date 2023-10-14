package br.com.blackjack.domain;

public interface Player {

    int points();

    boolean addCard(Card card);

    String name();

    void removeCards();

    static Player newPlayer(String name) {
        return new PlayerImpl(name);
    }
}
