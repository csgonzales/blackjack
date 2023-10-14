package br.com.blackjack;

import br.com.blackjack.domain.Match;

import java.io.IOException;
import java.util.logging.LogManager;

public class Main {

    static {
        try(var is = Main.class.getClassLoader().getResourceAsStream("logging.properties")) {
            LogManager.getLogManager().readConfiguration(is);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) throws InterruptedException {
        startMatch();
    }

    public static void startMatch() throws InterruptedException {
        final var match = new Match()
                .versusDealer();


        match.start();
        while(!match.isEnded()) {
            Thread.sleep(5000);
            match.draw();
        }
    }
}