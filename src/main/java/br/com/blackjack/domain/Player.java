package br.com.blackjack.domain;

import java.util.LinkedList;
import java.util.List;

public class Player {

    private final List<Card> cards = new LinkedList<>();
    private String name;

    public Player(String name) {
        this.name = name;
    }

    public int points() {
        return cards
                .stream()
                .mapToInt(Card::intValue)
                .sum();
    }

    public boolean addCard(Card card) {
        return cards.add(card);
    }

    public String name() {
        return name;
    }

    public void removeCards() {
        cards.clear();
    }
}
