package com.github.davidsmith.techtest.service.spins.outcome;

import com.github.davidsmith.techtest.model.SpinContext;
import com.github.davidsmith.techtest.model.SpinOutcomeResult;
import com.github.davidsmith.techtest.remote.entity.MachineEntity;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

class FullHouseOutcomeStrategyTest {

    private final FullHouseOutcomeStrategy fullHouseOutcomeStrategy = new FullHouseOutcomeStrategy();

    @Nested
    class Resolve {
        @Test
        void correctlyHalvesMachine() {
            final MachineEntity machine = MachineEntity.builder()
                .floatInPence(1000)
                .build();

            final SpinContext spinContext = SpinContext.builder()
                .machine(machine)
                .build();

            Assertions.assertThat(fullHouseOutcomeStrategy.resolve(spinContext))
                .returns(500, SpinOutcomeResult::getRemainingFloatInPence)
                .returns(null, SpinOutcomeResult::getFreeSpinsAwarded)
                .returns(500, SpinOutcomeResult::getPayoutInPence);

            Assertions.assertThat(machine)
                .returns(500, MachineEntity::getFloatInPence);
        }

        @Test
        void roundsMachineCorrectly_whenOddFloatValue() {
            final MachineEntity machine = MachineEntity.builder()
                .floatInPence(3)
                .build();

            final SpinContext spinContext = SpinContext.builder()
                .machine(machine)
                .build();

            Assertions.assertThat(fullHouseOutcomeStrategy.resolve(spinContext))
                .returns(1, SpinOutcomeResult::getRemainingFloatInPence)
                .returns(null, SpinOutcomeResult::getFreeSpinsAwarded)
                .returns(2, SpinOutcomeResult::getPayoutInPence);

            Assertions.assertThat(machine)
                .returns(1, MachineEntity::getFloatInPence);
        }

    }

}