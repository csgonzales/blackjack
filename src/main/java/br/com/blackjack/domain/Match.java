package br.com.blackjack.domain;

import br.com.blackjack.domain.exception.*;

import java.text.MessageFormat;
import java.util.*;
import java.util.logging.Logger;

public class Match {

    private static final Logger LOGGER = Logger.getLogger(Match.class.getName());

    private final Deck deck;
    private final List<Player> players = new ArrayList<>(4);
    private final Configurations configurations = new Configurations();
    private Player winner;
    private final Set<Player> losers = new HashSet<>();
    private int next = 0;
    private int limit;
    private final Map<Integer, Player> order = new HashMap<>();
    private boolean started = false;

    public Match(Deck deck) {
        this.deck = deck;
    }

    public Match() {
        this(Deck.newDeck());
    }

    public Match addPlayer(Player player) {
        if(!isStarted())
            players.add(player);
        return this;
    }

    public Match addPlayer(String playerName) {
        return addPlayer(Player.newPlayer(playerName));
    }

    public List<Player> players() {
        return Collections.unmodifiableList(players);
    }

    public void start() {
        if(players.size() < 2 && !configurations.isVersusDealer()) {
            throw new MatchRequiresAtLeastTwoPlayersException();
        } else if(players.size() > configurations.getPlayersLimit()) {
            throw new PlayersExceedLimitException(configurations().getPlayersLimit());
        }

        if(configurations.isVersusDealer()) {
            players.clear();
            log("Player vs Dealer ...");
            addPlayer("Player");
            addPlayer("Dealer");
        }
        log("Shuffling deck ...");
        deck.shuffle();
        log("Start drawing cards ...");
        for(var i = 0; i < players.size(); i++) {
            final var player = players.get(i);
            draw(player);
            order.put(i, player);
        }
        limit = players.size() - 1;
        started = true;
    }

    public void draw() {
        final var player = currentPlayer();
        draw(player);
        final var points = player.points();
        log("{0} owned {1} points.", player.name(), points);
        if(points == 21) {
            endMatch(player);
        } else if(player.points() > 21) {
            log("{0} lose ...", player.name());
            losers.add(player);
            if(losers.size() == limit) {
                endMatch(currentPlayer());
            }
        }
    }

    public boolean isStarted() {
        return started;
    }

    public boolean isEnded() {
        return Objects.nonNull(winner);
    }

    public Player winner() {
        return Optional
                .ofNullable(winner)
                .orElseThrow(MatchNotEndedException::new);
    }

    public Configurations configurations() {
        return configurations;
    }

    public Match aceAsEleven() {
        if(!isStarted()) {
            configurations.setAceAsEleven(true);
            log("Configure ace value as ELEVEN ...");
        }
        return this;
    }

    public Match aceAsOne() {
        if(!isStarted()) {
            configurations.setAceAsEleven(false);
            log("Configure ace value as ONE ...");
        }
        return this;
    }

    public Match playersLimit(int playersLimit) {
        if(!isStarted() && playersLimit >= 2) {
            configurations.setPlayersLimit(playersLimit);
            log("Configure players limit as {0} ...", playersLimit);
        }
        return this;
    }

    public Match versusDealer() {
        if(!isStarted()) {
            configurations.setVersusDealer(true);
            log("Configure Player versus Dealer ...");
        }
        return this;
    }

    public Match versusPlayers() {
        if(!isStarted()) {
            configurations.setVersusDealer(false);
            log("Configure Player versus Player ...");
        }
        return this;
    }

    private void draw(Player player) {
        final var card = adjustValue(deck.draw());
        log("{0} draw card: {1}", player.name(), card);
        player.addCard(card);
    }

    private Card adjustValue(Card card) {
        if(card.value() == Value.ACE && configurations().isAceAsEleven())
            return new Card(card.suit(), new AdjustedValue(card.value(), 11));
        return card;
    }

    private void log(String message, Object... values) {
        LOGGER.info(MessageFormat.format(message, values));
    }

    private void endMatch(Player winner) {
        this.winner = winner;
        log("{0} won!", winner.name());
        log("Match ended with {0} as winner.", winner.name());
    }

    private Player currentPlayer() {
        if(!started) {
            throw new MatchNotStartedException();
        } else if(isEnded()) {
            throw new MatchAlreadyEndedException();
        }

        final var player = order.get(next);
        configureNext();
        return losers.contains(player) ? currentPlayer() : player;
    }

    private void configureNext() {
        if(next == limit) {
            next = 0;
        } else {
            next++;
        }
    }
}
