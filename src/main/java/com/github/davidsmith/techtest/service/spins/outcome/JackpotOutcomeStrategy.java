package com.github.davidsmith.techtest.service.spins.outcome;

import com.github.davidsmith.techtest.model.SpinContext;
import com.github.davidsmith.techtest.model.SpinOutcome;
import com.github.davidsmith.techtest.model.SpinOutcomeResult;
import com.github.davidsmith.techtest.remote.entity.MachineEntity;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class JackpotOutcomeStrategy implements OutcomeStrategy {

    @Override
    public SpinOutcomeResult resolve(SpinContext spinContext) {
        log.debug("Paying out JACKPOT");

        final MachineEntity machine = spinContext.getMachine();
        final int payoutAmount = machine.getFloatInPence();

        machine.setFloatInPence(0);

        return SpinOutcomeResult.builder()
            .payoutInPence(payoutAmount)
            .remainingFloatInPence(machine.getFloatInPence())
            .build();
    }

    @Override
    public SpinOutcome supports() {
        return SpinOutcome.JACKPOT;
    }
}
