package br.com.blackjack.domain;

public record Card(Suit suit, CardValue value) {

    public int intValue() {
        return value.getVal();
    }
}
