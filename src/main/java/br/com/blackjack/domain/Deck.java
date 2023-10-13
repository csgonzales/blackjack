package br.com.blackjack.domain;

import br.com.blackjack.domain.exception.CardsNotShuffledException;
import br.com.blackjack.domain.exception.NoCardsLeftException;

import java.util.*;

public class Deck {

    private final List<Card> cards = new ArrayList<>(52);
    private Stack<Card> shuffledCards;

    public Deck() {
        for (Suit suit : Suit.values()) {
            for (Value value: Value.values()) {
                cards.add(new Card(suit, value));
            }
        }
    }

    public Deck shuffle() {
        shuffledCards = shuffleCards();
        return this;
    }

    private Stack<Card> shuffleCards() {
        final var stack = new Stack<Card>();
        final var addedCards = new HashSet<Card>();
        final var random = new Random();
        for (int i = 0; i < cards.size(); i++) {
            Card randomCard;
            do {
                randomCard = randomCard(random);
            } while (addedCards.contains(randomCard));
            addedCards.add(randomCard);
            stack.push(randomCard);
        }
        return stack;
    }

    private Card randomCard(Random random) {
        return cards.get(random.nextInt(cards.size()));
    }

    public Card draw() {
        try {
            return Optional
                    .ofNullable(shuffledCards)
                    .map(Stack::pop)
                    .orElseThrow(CardsNotShuffledException::new);
        } catch (EmptyStackException e) {
            throw new NoCardsLeftException();
        }
    }
}
