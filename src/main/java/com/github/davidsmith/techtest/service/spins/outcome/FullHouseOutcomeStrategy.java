package com.github.davidsmith.techtest.service.spins.outcome;

import com.github.davidsmith.techtest.model.SpinContext;
import com.github.davidsmith.techtest.model.SpinOutcome;
import com.github.davidsmith.techtest.model.SpinOutcomeResult;
import com.github.davidsmith.techtest.remote.entity.MachineEntity;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.math.RoundingMode;

@Slf4j
@Component
public class FullHouseOutcomeStrategy implements OutcomeStrategy {

    @Override
    public SpinOutcomeResult resolve(SpinContext spinContext) {
        log.debug("Paying out FULL_HOUSE");

        final MachineEntity machine = spinContext.getMachine();
        final int payoutAmount = BigDecimal.valueOf(machine.getFloatInPence())
            .divide(BigDecimal.valueOf(2), RoundingMode.CEILING)
            .intValue();

        machine.setFloatInPence(machine.getFloatInPence() - payoutAmount);

        return SpinOutcomeResult.builder()
            .remainingFloatInPence(machine.getFloatInPence())
            .payoutInPence(payoutAmount)
            .build();
    }

    @Override
    public SpinOutcome supports() {
        return SpinOutcome.FULL_HOUSE;
    }
}
