package br.com.blackjack.domain;

public record AdjustedValue(CardValue value, int adjustedValue) implements CardValue {

    @Override
    public int getVal() {
        return adjustedValue;
    }

    @Override
    public String toString() {
        return value.toString();
    }
}
