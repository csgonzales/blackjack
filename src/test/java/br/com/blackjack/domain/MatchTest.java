package br.com.blackjack.domain;

import br.com.blackjack.domain.exception.*;
import org.junit.jupiter.api.Test;

import java.util.Stack;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.*;

class MatchTest {

    @Test
    void shouldCreateAMatchThatIsNotStarted() {
        final var match = new Match();

        assertFalse(match.isStarted());
        assertFalse(match.isEnded());
    }

    @Test
    void shouldFailToStartMatchWithoutTwoPlayers() {
        final var match = new Match().addPlayer("Player 1");

        final var exception = assertThrows(MatchRequiresAtLeastTwoPlayersException.class, match::start);
        assertThat(exception, notNullValue());
        assertThat(exception.getMessage(), is("Match requires at least two players!"));
    }

    @Test
    void shouldFailToDrawCardWhenMatchIsNotStarted() {
        final var match = new Match()
                .addPlayer("Player 1")
                .addPlayer("Player 2");

        final var exception = assertThrows(MatchNotStartedException.class, match::draw);
        assertThat(exception, notNullValue());
        assertThat(exception.getMessage(), is("Match not started!"));
    }

    @Test
    void shouldFailToGetWinnerWhenMatchIsNotEnded() {
        final var match = new Match()
                .addPlayer("Player 1")
                .addPlayer("Player 2");

        final var exception = assertThrows(MatchNotEndedException.class, match::winner);
        assertThat(exception, notNullValue());
        assertThat(exception.getMessage(), is("Match not ended!"));
    }

    @Test
    void shouldStartMatchAndDrawCards() {
        final var match = new Match()
                .addPlayer("Player 1")
                .addPlayer("Player 2");

        match.start();
        match.draw();
        match.draw();

        assertTrue(match.isStarted());
        assertFalse(match.isEnded());
    }

    @Test
    void shouldFailToDrawCardAfterMatchIsEnded() {
        final var match = new Match()
                .addPlayer("Player 1")
                .addPlayer("Player 2");

        match.start();
        while(!match.isEnded()) {
            match.draw();
        }

        assertTrue(match.isEnded());

        final var exception = assertThrows(MatchAlreadyEndedException.class, match::draw);
        assertThat(exception, notNullValue());
        assertThat(exception.getMessage(), is("Match already ended!"));
    }

    @Test
    void shouldStartMatchAndEndItWithWinner() {
        final var match = new Match()
                .addPlayer("Player 1")
                .addPlayer("Player 2");

        match.start();
        while(!match.isEnded()) {
            match.draw();
        }

        assertTrue(match.isEnded());
        assertThat(match.winner(), notNullValue());
    }

    @Test
    void shouldStartMatchAndEndItWhenPlayerWinsWith21Points() {
        final var player = Player.newPlayer("Winner");
        final var match = new Match(deckThatAlwaysHit21())
                .addPlayer(player)
                .addPlayer("Loser");

        match.start();
        while(!match.isEnded()) {
            match.draw();
        }

        assertTrue(match.isEnded());
        final var winner = match.winner();
        assertThat(winner, notNullValue());
        assertThat(winner, is(player));
        assertThat(winner.points(), is(21));
    }

    @Test
    void shouldStartMatchAndEndItWhenLoserGetsMoreThan21Points() {
        final var player = Player.newPlayer("Winner");
        final var match = new Match(deckThatAlwaysGetMorePoints())
                .addPlayer(player)
                .addPlayer("Loser");

        match.start();
        while(!match.isEnded()) {
            match.draw();
        }

        assertTrue(match.isEnded());
        final var winner = match.winner();
        assertThat(winner, notNullValue());
        assertThat(winner, is(player));
        assertThat(winner.points(), is(14));
    }

    @Test
    void shouldStartMatchCreatingPlayersWhenVersusDealerIsTrue() {
        final var match = new Match()
                .versusDealer();

        match.start();

        final var configurations = match.configurations();
        assertThat(configurations, notNullValue());
        assertThat(configurations.isVersusDealer(), is(true));
        assertThat(match.players(), hasSize(2));
    }

    @Test
    void shouldConsiderLastVersusConfiguration() {
        final var match = new Match()
                .versusDealer();

        final var configurations = match.configurations();
        assertThat(configurations, notNullValue());
        assertThat(configurations.isVersusDealer(), is(true));

        match.versusPlayers();
        assertThat(configurations.isVersusDealer(), is(false));
    }

    @Test
    void shouldIgnoreAddedPlayersWhenVersusDealerConfigurationIsEnabled() {
        final var match = new Match()
                .addPlayer("Player 1")
                .addPlayer("Player 2")
                .addPlayer("Player 3")
                .versusDealer();

        match.start();
        final var players = match.players();
        assertThat(players, hasSize(2));
        assertThat(players.get(0).name(), is("Player"));
        assertThat(players.get(1).name(), is("Dealer"));
    }

    @Test
    void shouldIgnoreNewPlayersWhenMatchIsStarted() {
        final var match = new Match()
                .addPlayer("Test 1")
                .addPlayer("Test 2");

        match.start();
        match.addPlayer("Test 3");

        final var players = match.players();
        assertThat(players, hasSize(2));
        assertThat(players.get(0).name(), is("Test 1"));
        assertThat(players.get(1).name(), is("Test 2"));
    }

    @Test
    void shouldConfigureAcePointsAsEleven() {
        final var player = Player.newPlayer("Test");
        final var match = new Match(deckThatAlwaysHit21())
                .aceAsEleven()
                .addPlayer(player)
                .addPlayer("Dealer");

        match.start();
        assertThat(player.points(), is(11));
    }

    @Test
    void shouldConfigureAcePointsAsOne() {
        final var player = Player.newPlayer("Test");
        final var match = new Match(deckThatAlwaysDrawsAces())
                .aceAsEleven()
                .addPlayer(player)
                .addPlayer("Dealer")
                .aceAsOne();

        match.start();

        assertThat(player.points(), is(1));
    }

    @Test
    void shouldIgnoreAceAsOneConfigurationWhenMatchIsStarted() {
        final var player = Player.newPlayer("Test");
        final var match = new Match(deckThatAlwaysDrawsAces())
                .aceAsEleven()
                .addPlayer(player)
                .addPlayer("Dealer");

        match.start();
        match.aceAsOne();
        match.draw();

        final var lastCard = player.cards().get(1);
        assertThat(lastCard.intValue(), is(11));
    }

    @Test
    void shouldIgnoreAceAsElevenConfigurationWhenMatchIsStarted() {
        final var player = Player.newPlayer("Test");
        final var match = new Match(deckThatAlwaysDrawsAces())
                .addPlayer(player)
                .addPlayer("Dealer");

        match.start();
        match.aceAsEleven();
        match.draw();

        final var lastCard = player.cards().get(1);
        assertThat(lastCard.intValue(), is(1));
    }

    @Test
    void shouldThrowExceptionWhenMatchExceedsPlayersAmount() {
        final var match = new Match()
                .playersLimit(2)
                .addPlayer("Test 1")
                .addPlayer("Test 2")
                .addPlayer("Test 3");

        final var exception = assertThrows(PlayersExceedLimitException.class, match::start);
        assertThat(exception.getMessage(), is("Players exceed limit of 2!"));
    }

    @Test
    void shouldIgnoreWhenPlayersLimitIsLowerThanTwo() {
        final var match = new Match()
                .playersLimit(1)
                .addPlayer("Test 1")
                .addPlayer("Test 2")
                .addPlayer("Test 3");

        match.start();

        final var configurations = match.configurations();
        assertThat(configurations.getPlayersLimit(), is(4));
    }

    @Test
    void shouldIgnoreWhenPlayersLimitIsConfiguredAfterMatchIsStarted() {
        final var match = new Match()
                .addPlayer("Test 1")
                .addPlayer("Test 2")
                .addPlayer("Test 3");

        match.start();
        match.playersLimit(2);

        final var configurations = match.configurations();
        assertThat(configurations.getPlayersLimit(), is(4));
    }

    private Deck deckThatAlwaysDrawsAces() {
        return new FakeDeck(new Stack<>() {{
            push(new Card(Suit.SPADES, Value.ACE));
            push(new Card(Suit.DIAMONDS, Value.ACE));
            push(new Card(Suit.CLUBS, Value.ACE));
            push(new Card(Suit.HEARTS, Value.ACE));
        }});
    }

    private Deck deckThatAlwaysHit21() {
        return new FakeDeck(new Stack<>() {{
            push(new Card(Suit.DIAMONDS, Value.KING));
            push(new Card(Suit.SPADES, Value.KING));
            push(new Card(Suit.DIAMONDS, Value.JACK));
            push(new Card(Suit.CLUBS, Value.QUEEN));
            push(new Card(Suit.HEARTS, Value.ACE));
        }});
    }

    private Deck deckThatAlwaysGetMorePoints() {
        return new FakeDeck(new Stack<>() {{
            push(new Card(Suit.CLUBS, Value.KING));
            push(new Card(Suit.DIAMONDS, Value.THREE));
            push(new Card(Suit.SPADES, Value.KING));
            push(new Card(Suit.DIAMONDS, Value.ACE));
            push(new Card(Suit.CLUBS, Value.QUEEN));
            push(new Card(Suit.HEARTS, Value.JACK));
        }});
    }

    private record FakeDeck(Stack<Card> cards) implements Deck {

        @Override
        public void shuffle() {
        }

        @Override
        public Card draw() {
            return cards.pop();
        }
    }

}