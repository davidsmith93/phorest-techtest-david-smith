package com.github.davidsmith.techtest.service.spins.outcome;

import com.github.davidsmith.techtest.model.SpinContext;
import com.github.davidsmith.techtest.model.SpinOutcomeResult;
import com.github.davidsmith.techtest.remote.entity.MachineEntity;
import com.github.davidsmith.techtest.remote.entity.UserSpinsEntity;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

class SmallPrizeOutcomeStrategyTest {

    private final SmallPrizeOutcomeStrategy smallPrizeOutcomeStrategy = new SmallPrizeOutcomeStrategy();

    @Nested
    class Resolve {
        @MethodSource("resolveParameters")
        @ParameterizedTest
        void paysOutCorrectAmount_whenPayoutAmountIsCoveredByFloat(
            int initialFloat,
            int spinPrice,
            int initialFreeSpins,
            int expectedFloat,
            int expectedPayout,
            int expectedFreeSpins
        ) {
            final MachineEntity machine = createMachine(initialFloat, spinPrice);
            final UserSpinsEntity userSpins = createUserSpinsEntity(initialFreeSpins);
            final SpinContext spinContext = createSpinContext(machine, userSpins);

            Assertions.assertThat(smallPrizeOutcomeStrategy.resolve(spinContext))
                .returns(expectedFloat, SpinOutcomeResult::getRemainingFloatInPence)
                .returns(expectedPayout, SpinOutcomeResult::getPayoutInPence)
                .returns(expectedFreeSpins, SpinOutcomeResult::getFreeSpinsAwarded);

            Assertions.assertThat(machine)
                .returns(expectedFloat, MachineEntity::getFloatInPence);

            Assertions.assertThat(userSpins)
                .returns(expectedFreeSpins, UserSpinsEntity::getFreeSpins);
        }

        private static Stream<Arguments> resolveParameters() {
            return Stream.of(
                Arguments.of(1000, 100, 0, 500, 500, 0),
                Arguments.of(500, 100, 0, 0, 500, 0),
                Arguments.of(100, 100, 0, 0, 100, 4),
                Arguments.of(99, 100, 0, 0, 99, 4),
                Arguments.of(1, 100, 0, 0, 1, 4),
                Arguments.of(101, 100, 0, 0, 101, 3)
            );
        }

        private MachineEntity createMachine(int floatInPence, int spinPriceInPence) {
            return MachineEntity.builder()
                .floatInPence(floatInPence)
                .spinPriceInPence(spinPriceInPence)
                .build();
        }

        private UserSpinsEntity createUserSpinsEntity(int freeSpins) {
            return UserSpinsEntity.builder()
                .freeSpins(freeSpins)
                .build();
        }

        private SpinContext createSpinContext(MachineEntity machine, UserSpinsEntity userSpins) {
            return SpinContext.builder()
                .machine(machine)
                .userSpins(userSpins)
                .build();
        }
    }


}