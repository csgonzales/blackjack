package br.com.blackjack.domain;

public record Card(Suit suit, Value value) {

    public int intValue() {
        return value.getVal();
    }
}
