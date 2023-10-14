package br.com.blackjack.domain;

import java.util.LinkedList;
import java.util.List;

public class PlayerImpl implements Player {

    private final List<Card> cards = new LinkedList<>();
    private String name;

    public PlayerImpl(String name) {
        this.name = name;
    }

    @Override
    public int points() {
        return cards
                .stream()
                .mapToInt(Card::intValue)
                .sum();
    }

    @Override
    public boolean addCard(Card card) {
        return cards.add(card);
    }

    @Override
    public String name() {
        return name;
    }

    @Override
    public void removeCards() {
        cards.clear();
    }
}
