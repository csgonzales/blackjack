package br.com.blackjack;

import br.com.blackjack.domain.Match;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
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
        helper(args);
        startMatch(new Configurations(args));
    }

    public static void helper(String[] args) {
        var showInfo = false;
        for (var arg : args) {
            if(arg.equals("h") || arg.equals("help")) {
                showInfo = true;
                break;
            }
        }
        if(showInfo) {
            final var description =
                    """
                    Valid options:
                    
                    aceAsEleven 
                        description: Set aces with 11 as value.
                        example:
                            java -jar blackjack-1.0-SNAPSHOT-jar-with-dependencies.jar aceAsEleven
                            
                    players
                        description: Configures players in match, separated by COMMA (,).
                        example:
                            java -jar blackjack-1.0-SNAPSHOT-jar-with-dependencies.jar "players=Player 1, Player 2, Player 3, Player 4"
                    
                    versusDealer
                        description: Configures match with Player vs Dealer.
                        example:
                            java -jar blackjack-1.0-SNAPSHOT-jar-with-dependencies.jar versusDealer
                            
                    playersLimit
                        description: Configures limit of players in match. Int value.
                        example:
                            java -jar blackjack-1.0-SNAPSHOT-jar-with-dependencies.jar playersLimit=2
                            
                    ____________________________________________________________________________________________________
                    To return here, just use -h or --help again
                    
                        example:
                            java -jar blackjack-1.0-SNAPSHOT-jar-with-dependencies.jar h
                    """;
            System.out.println(description);
            System.exit(0);
        }
    }

    public static void startMatch(Configurations configurations) throws InterruptedException {
        final var match = new Match()
                .versusDealer();

        if(configurations.hasParam(Configurations.ACE_AS_ELEVEN)) {
            match.aceAsEleven();
        }

        if(configurations.hasParam(Configurations.PLAYERS)) {
            final var players = configurations.getParam(Configurations.PLAYERS).split(",");
            for (var player : players) {
                match.addPlayer(player.trim());
            }
            match.versusPlayers();
        }

        if(configurations.hasParam(Configurations.VERSUS_DEALER)) {
            match.versusDealer();
        }

        if(configurations.hasParam(Configurations.PLAYERS_LIMIT)) {
            match.playersLimit(Integer.parseInt(configurations.getParam(Configurations.PLAYERS_LIMIT)));
        }

        match.start();
        while(!match.isEnded()) {
            Thread.sleep(5000);
            match.draw();
        }
    }

    static class Configurations {

        static final String ACE_AS_ELEVEN = "aceAsEleven";
        static final String VERSUS_DEALER = "versusDealer";
        static final String PLAYERS_LIMIT = "playersLimit";
        static final String PLAYERS = "players";

        private final Map<String, Object> configs = new HashMap<>();

        Configurations(String[] args) {
            for (String arg: args) {
                configureParam(arg);
            }
        }

        public boolean hasParam(String param) {
            return configs.containsKey(param);
        }

        public String getParam(String param) {
            final var value = configs.get(param);
            return Objects.nonNull(value) ? String.valueOf(value) : null;
        }

        private void configureParam(String value) {
            if(!value.isBlank()) {
                final var keyValue = value.split("=");
                if(keyValue.length == 2) {
                    configs.put(keyValue[0], keyValue[1]);
                } else if(keyValue.length == 1) {
                    configs.put(keyValue[0], true);
                }
            }
        }
    }
}