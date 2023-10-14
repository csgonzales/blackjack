package br.com.blackjack.domain;

import java.util.List;

public interface Player {

    int points();

    boolean addCard(Card card);

    String name();

    List<Card> cards();

    void removeCards();

    static Player newPlayer(String name) {
        return new PlayerImpl(name);
    }
}
