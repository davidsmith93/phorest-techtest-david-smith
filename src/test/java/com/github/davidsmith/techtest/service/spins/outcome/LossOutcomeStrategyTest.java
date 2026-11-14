package com.github.davidsmith.techtest.service.spins.outcome;

import com.github.davidsmith.techtest.model.SpinContext;
import com.github.davidsmith.techtest.model.SpinOutcomeResult;
import com.github.davidsmith.techtest.remote.entity.MachineEntity;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

class LossOutcomeStrategyTest {

    private final LossOutcomeStrategy lossOutcomeStrategy = new LossOutcomeStrategy();

    @Nested
    class Resolve {
        @Test
        void returnsSameValues() {
            final MachineEntity machine = MachineEntity.builder()
                .floatInPence(1000)
                .build();

            final SpinContext spinContext = SpinContext.builder()
                .machine(machine)
                .build();

            Assertions.assertThat(lossOutcomeStrategy.resolve(spinContext))
                .returns(1000, SpinOutcomeResult::getRemainingFloatInPence)
                .returns(null, SpinOutcomeResult::getFreeSpinsAwarded)
                .returns(null, SpinOutcomeResult::getPayoutInPence);

            Assertions.assertThat(machine)
                .returns(1000, MachineEntity::getFloatInPence);
        }
    }

}