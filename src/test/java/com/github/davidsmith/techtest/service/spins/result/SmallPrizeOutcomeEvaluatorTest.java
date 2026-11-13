package com.github.davidsmith.techtest.service.spins.result;

import com.github.davidsmith.techtest.model.SpinOutcome;
import com.github.davidsmith.techtest.remote.entity.MachineEntity;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.Mockito;

import java.util.List;
import java.util.stream.Stream;

class SmallPrizeOutcomeEvaluatorTest {

    private final SmallPrizeOutcomeEvaluator smallPrizeOutcomeEvaluator = new SmallPrizeOutcomeEvaluator();

    @Nested
    class Evaluate {
        @MethodSource("evaluateParameters")
        @ParameterizedTest
        void returnsExpectedOutcome(List<String> actualColours, int adjacency, SpinOutcome outcome) {
            final MachineEntity machineEntity = Mockito.mock();
            Mockito.doReturn(adjacency).when(machineEntity).getSmallPrizeAdjacency();

            Assertions.assertThat(smallPrizeOutcomeEvaluator.evaluate(machineEntity, actualColours))
                .isEqualTo(outcome);
        }

        private static Stream<Arguments> evaluateParameters() {
            return Stream.of(
                Arguments.of(
                    List.of("BLACK", "WHITE", "BLACK", "BLACK"),
                    2,
                    SpinOutcome.SMALL_PRIZE
                ),
                Arguments.of(
                    List.of("GREEN", "BLACK", "BLACK", "BLACK", "BLACK", "GREEN"),
                    4,
                    SpinOutcome.SMALL_PRIZE
                ),
                Arguments.of(
                    List.of("BLACK", "WHITE", "BLACK", "WHITE"),
                    2,
                    SpinOutcome.LOSS
                ),
                Arguments.of(
                    List.of("BLACK", "WHITE"),
                    4,
                    SpinOutcome.LOSS
                ),
                Arguments.of(
                    List.of("BLACK", "BLACK"),
                    4,
                    SpinOutcome.LOSS
                )
            );
        }
    }

}