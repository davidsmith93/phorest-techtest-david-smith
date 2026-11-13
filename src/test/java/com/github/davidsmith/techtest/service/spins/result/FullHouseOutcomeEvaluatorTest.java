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
import java.util.Set;
import java.util.stream.Stream;

class FullHouseOutcomeEvaluatorTest {

    private final FullHouseOutcomeEvaluator fullHouseOutcomeEvaluator = new FullHouseOutcomeEvaluator();

    @Nested
    class Evaluate {
        @MethodSource("evaluateParameters")
        @ParameterizedTest
        void returnsExpectedOutcome(Set<String> machineColours, List<String> actualColours, SpinOutcome outcome) {
            final MachineEntity machineEntity = Mockito.mock();
            Mockito.doReturn(machineColours).when(machineEntity).getColours();
            Mockito.doReturn(actualColours.size()).when(machineEntity).getNumberOfSlots();

            Assertions.assertThat(fullHouseOutcomeEvaluator.evaluate(machineEntity, actualColours))
                .isEqualTo(outcome);
        }

        private static Stream<Arguments> evaluateParameters() {
            return Stream.of(
                Arguments.of(
                    Set.of("BLACK", "WHITE"),
                    List.of("BLACK", "WHITE", "BLACK", "BLACK"),
                    SpinOutcome.FULL_HOUSE
                ),
                Arguments.of(
                    Set.of("BLACK", "WHITE", "GREEN", "YELLOW"),
                    List.of("BLACK", "WHITE", "YELLOW", "GREEN"),
                    SpinOutcome.FULL_HOUSE
                ),
                Arguments.of(
                    Set.of("BLACK", "WHITE", "GREEN"),
                    List.of("BLACK", "WHITE"),
                    SpinOutcome.FULL_HOUSE
                ),
                Arguments.of(
                    Set.of("BLACK", "WHITE"),
                    List.of("BLACK", "BLACK"),
                    SpinOutcome.LOSS
                )
            );
        }
    }

}