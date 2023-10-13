package br.com.blackjack.domain;

import br.com.blackjack.domain.exception.CardsNotShuffledException;

import br.com.blackjack.domain.exception.NoCardsLeftException;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.*;

class DeckTest {

    @Test
    void shouldShuffleDeckAndGetDifferentDrawsMostOfTimes() {
        final var deck = new Deck();

        final var drawsFromFirstShuffle = shuffleAndDraw3Cards(deck);
        final var drawsFromSecondShuffle = shuffleAndDraw3Cards(deck);
        final var drawsFromThirdShuffle = shuffleAndDraw3Cards(deck);

        boolean somethingIsDifferent = false;
        for (int i = 0; i < 3; i++) {
            final var firstCard = drawsFromFirstShuffle.get(i);
            final var secondCard = drawsFromSecondShuffle.get(i);
            final var thirdCard = drawsFromThirdShuffle.get(i);
            if(firstCard != secondCard || secondCard != thirdCard) {
                somethingIsDifferent = true;
                break;
            }
        }
        assertTrue(somethingIsDifferent);
    }

    @Test
    void shouldFailToDrawCardsWhenThenAreNotShuffled() {
        final var deck = new Deck();

        final var exception = assertThrows(CardsNotShuffledException.class, deck::draw);
        assertThat(exception, notNullValue());
        assertThat(exception.getMessage(), is("Cards not shuffled yet!"));
    }

    @Test
    void shouldFailToDrawCardsWhenNoOneLeft() {
        final var deck = new Deck();

        deck.shuffle();
        for (int i = 0; i < 52; i++) {
            deck.draw();
        }
        final var exception = assertThrows(NoCardsLeftException.class, deck::draw);
        assertThat(exception, notNullValue());
        assertThat(exception.getMessage(), is("No cards left!"));
    }

    private List<Card> shuffleAndDraw3Cards(Deck deck) {
        deck.shuffle();
        return new ArrayList<Card>(3) {{
            add(deck.draw());
            add(deck.draw());
            add(deck.draw());
        }};
    }

}