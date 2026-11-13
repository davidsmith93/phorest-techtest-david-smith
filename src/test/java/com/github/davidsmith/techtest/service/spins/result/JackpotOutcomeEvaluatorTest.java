package com.github.davidsmith.techtest.service.spins.result;

import com.github.davidsmith.techtest.model.SpinOutcome;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.List;


class JackpotOutcomeEvaluatorTest {

    private final JackpotOutcomeEvaluator jackpotOutcomeEvaluator = new JackpotOutcomeEvaluator();

    @Nested
    class Evaluate {
        @Test
        void returnsJackpot_whenAllSlotsMatch() {
            Assertions.assertThat(
                jackpotOutcomeEvaluator.evaluate(Mockito.mock(), List.of("MATCH", "MATCH", "MATCH", "MATCH"))
            ).isEqualTo(SpinOutcome.JACKPOT);
        }

        @Test
        void returnsLoss_whenAllSlotsDoNotMatch() {
            Assertions.assertThat(
                jackpotOutcomeEvaluator.evaluate(Mockito.mock(), List.of("MATCH", "MATCH", "MATCH", "match"))
            ).isEqualTo(SpinOutcome.LOSS);
        }
    }

}