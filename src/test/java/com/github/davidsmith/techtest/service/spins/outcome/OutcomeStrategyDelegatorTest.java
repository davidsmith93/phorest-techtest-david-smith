package com.github.davidsmith.techtest.service.spins.outcome;

import com.github.davidsmith.techtest.exception.StrategyNotFoundException;
import com.github.davidsmith.techtest.model.SpinContext;
import com.github.davidsmith.techtest.model.SpinOutcome;
import com.github.davidsmith.techtest.model.SpinOutcomeResult;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.List;

class OutcomeStrategyDelegatorTest {

    private final OutcomeStrategy firstStrategy = mockOutcomeStrategy(SpinOutcome.JACKPOT);
    private final OutcomeStrategy secondStrategy = mockOutcomeStrategy(SpinOutcome.LOSS);

    private final OutcomeStrategyDelegator outcomeStrategyDelegator = new OutcomeStrategyDelegator(
        List.of(firstStrategy, secondStrategy)
    );

    @Nested
    class Delegate {
        @Test
        void callsCorrectStrategy() {
            final SpinContext spinContext = SpinContext.builder()
                .spinOutcome(SpinOutcome.LOSS)
                .build();

            final SpinOutcomeResult spinOutcomeResult = Mockito.mock();

            Mockito.doReturn(spinOutcomeResult)
                .when(secondStrategy)
                .resolve(spinContext);

            Assertions.assertThat(outcomeStrategyDelegator.delegate(spinContext))
                .isEqualTo(spinOutcomeResult);

            Mockito.verify(secondStrategy).resolve(spinContext);
            Mockito.verify(firstStrategy, Mockito.never()).resolve(Mockito.any());
        }

        @Test
        void throwsException_whenStrategyNotFound() {
            final SpinContext spinContext = SpinContext.builder()
                .spinOutcome(SpinOutcome.FULL_HOUSE)
                .build();

            Assertions.assertThatThrownBy(() -> outcomeStrategyDelegator.delegate(spinContext))
                .isExactlyInstanceOf(StrategyNotFoundException.class);

            Mockito.verify(firstStrategy, Mockito.never()).resolve(Mockito.any());
            Mockito.verify(secondStrategy, Mockito.never()).resolve(Mockito.any());
        }
    }

    private static OutcomeStrategy mockOutcomeStrategy(SpinOutcome spinOutcome) {
        final OutcomeStrategy outcomeStrategy = Mockito.mock();
        Mockito.doReturn(spinOutcome).when(outcomeStrategy).supports();

        return outcomeStrategy;
    }
}