package br.com.blackjack.domain;

import org.junit.jupiter.api.Test;

import static org.hamcrest.Matchers.*;
import static org.hamcrest.MatcherAssert.*;

class PlayerTest {

    @Test
    void shouldCreatePlayerAndValidatePoints() {
        final var player = new PlayerImpl("Test");

        player.addCard(new Card(Suit.HEARTS, Value.JACK));
        player.addCard(new Card(Suit.SPADES, Value.ACE));
        player.addCard(new Card(Suit.CLUBS, Value.FOUR));

        assertThat(player.name(), is("Test"));
        assertThat(player.points(), is(15));
    }

    @Test
    void shouldGuaranteeThatPlayerHasNoPointsAfterRemoveCards() {
        final var player = new PlayerImpl("Test");

        player.addCard(new Card(Suit.HEARTS, Value.JACK));
        player.addCard(new Card(Suit.SPADES, Value.ACE));
        player.addCard(new Card(Suit.CLUBS, Value.FOUR));

        assertThat(player.name(), is("Test"));
        assertThat(player.points(), is(15));

        player.removeCards();

        assertThat(player.name(), is("Test"));
        assertThat(player.points(), is(0));
    }

}