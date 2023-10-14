package br.com.blackjack.domain;

import br.com.blackjack.domain.exception.MatchAlreadyEndedException;
import br.com.blackjack.domain.exception.MatchNotEndedException;
import br.com.blackjack.domain.exception.MatchNotStartedException;
import br.com.blackjack.domain.exception.MatchRequiresAtLeastTwoPlayersException;
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
        final var player = new PlayerImpl("Winner");
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
        final var player = new PlayerImpl("Winner");
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

    private Deck deckThatAlwaysHit21() {
        final var stack = new Stack<Card>() {{
           push(new Card(Suit.DIAMONDS, Value.KING));
           push(new Card(Suit.SPADES, Value.KING));
           push(new Card(Suit.DIAMONDS, Value.JACK));
           push(new Card(Suit.CLUBS, Value.QUEEN));
           push(new Card(Suit.HEARTS, Value.ACE));
        }};
        return new FakeDeck(stack);
    }

    private Deck deckThatAlwaysGetMorePoints() {
        final var stack = new Stack<Card>() {{
            push(new Card(Suit.CLUBS, Value.KING));
            push(new Card(Suit.DIAMONDS, Value.THREE));
            push(new Card(Suit.SPADES, Value.KING));
            push(new Card(Suit.DIAMONDS, Value.ACE));
            push(new Card(Suit.CLUBS, Value.QUEEN));
            push(new Card(Suit.HEARTS, Value.JACK));
        }};
        return new FakeDeck(stack);
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