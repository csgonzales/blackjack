package br.com.blackjack.domain;

public class Configurations {

    private boolean aceAsEleven = false;
    private int playersLimit = 4;
    private boolean versusDealer = false;

    public boolean isAceAsEleven() {
        return aceAsEleven;
    }

    void setAceAsEleven(boolean aceAsEleven) {
        this.aceAsEleven = aceAsEleven;
    }

    public int getPlayersLimit() {
        return playersLimit;
    }

    void setPlayersLimit(int playersLimit) {
        this.playersLimit = playersLimit;
    }

    public boolean isVersusDealer() {
        return versusDealer;
    }

    void setVersusDealer(boolean versusDealer) {
        this.versusDealer = versusDealer;
    }
}
