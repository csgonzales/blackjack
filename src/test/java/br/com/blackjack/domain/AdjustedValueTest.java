package br.com.blackjack.domain;

import org.hamcrest.MatcherAssert;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;

import static org.hamcrest.MatcherAssert.*;
import static org.hamcrest.Matchers.is;

class AdjustedValueTest {

    @Test
    void shouldConsiderAdjustedValue() {
        final var adjustedValue = new AdjustedValue(Value.ACE, 11);

        assertThat(Value.ACE.getVal(), is(1));
        assertThat(adjustedValue.value(), is(Value.ACE));
        assertThat(adjustedValue.toString(), is(Value.ACE.toString()));
        assertThat(adjustedValue.getVal(), is(11));
    }

}