package com.github.davidsmith.techtest.service.spins.result;

import com.github.davidsmith.techtest.model.SpinOutcome;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

@ExtendWith(MockitoExtension.class)
class CompositeOutcomeEvaluatorTest {

    @Mock
    JackpotOutcomeEvaluator jackpotOutcomeEvaluator;

    @InjectMocks
    CompositeOutcomeEvaluator compositeOutcomeEvaluator;

    @Nested
    class Evaluate {
        @Test
        void returnsResultFromJackpotOutcomeEvaluator() {
            final List<String> slots = Mockito.mock();

            Mockito.doReturn(SpinOutcome.JACKPOT)
                .when(jackpotOutcomeEvaluator)
                .evaluate(slots);

            Assertions.assertThat(compositeOutcomeEvaluator.evaluate(slots))
                .isEqualTo(SpinOutcome.JACKPOT);
        }
    }

}