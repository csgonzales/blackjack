package br.com.blackjack.domain;

public enum Value {

    ACE(1),
    TWO(2),
    THREE(3),
    FOUR(4),
    FIVE(5),
    SIX(6),
    SEVEN(7),
    EIGHT(8),
    NINE(9),
    TEN(10),
    JACK(10),
    QUEEN(10),
    KING(10);

    Value(int val) {
        this.val = val;
    }

    private final int val;

    public int getVal() {
        return val;
    }
}
