package com.github.davidsmith.techtest.service.spins.outcome;

import com.github.davidsmith.techtest.model.SpinContext;
import com.github.davidsmith.techtest.model.SpinOutcomeResult;
import com.github.davidsmith.techtest.remote.entity.MachineEntity;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

class JackpotOutcomeStrategyTest {

    private final JackpotOutcomeStrategy jackpotOutcomeStrategy = new JackpotOutcomeStrategy();

    @Nested
    class Resolve {
        @Test
        void correctlyZerosMachine() {
            final MachineEntity machine = MachineEntity.builder()
                .floatInPence(1000)
                .build();

            final SpinContext spinContext = SpinContext.builder()
                .machine(machine)
                .build();

            Assertions.assertThat(jackpotOutcomeStrategy.resolve(spinContext))
                .returns(0, SpinOutcomeResult::getRemainingFloatInPence)
                .returns(null, SpinOutcomeResult::getFreeSpinsAwarded)
                .returns(1000, SpinOutcomeResult::getPayoutInPence);

            Assertions.assertThat(machine)
                .returns(0, MachineEntity::getFloatInPence);
        }
    }

}