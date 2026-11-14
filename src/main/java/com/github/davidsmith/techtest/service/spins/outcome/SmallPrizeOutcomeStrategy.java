package com.github.davidsmith.techtest.service.spins.outcome;

import com.github.davidsmith.techtest.model.SpinContext;
import com.github.davidsmith.techtest.model.SpinOutcome;
import com.github.davidsmith.techtest.model.SpinOutcomeResult;
import com.github.davidsmith.techtest.remote.entity.MachineEntity;
import com.github.davidsmith.techtest.remote.entity.UserSpinsEntity;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class SmallPrizeOutcomeStrategy implements OutcomeStrategy {

    private static final int SMALL_PRIZE_MULTIPLIER = 5;

    @Override
    public SpinOutcomeResult resolve(SpinContext spinContext) {
        log.debug("Paying out SMALL_PRIZE");

        final MachineEntity machine = spinContext.getMachine();
        final UserSpinsEntity userSpins = spinContext.getUserSpins();

        final int targetPayoutAmount = machine.getSpinPriceInPence() * SMALL_PRIZE_MULTIPLIER;

        int freeSpins = 0;
        int machineFloat = machine.getFloatInPence() - targetPayoutAmount;

        if(machineFloat < 0) {
            freeSpins = (machineFloat * -1) / machine.getSpinPriceInPence();
            machineFloat = 0;
        }

        final int payoutAmount = machine.getFloatInPence() - machineFloat;

        machine.setFloatInPence(machineFloat);
        userSpins.setFreeSpins(userSpins.getFreeSpins() + freeSpins);

        return SpinOutcomeResult.builder()
            .remainingFloatInPence(machine.getFloatInPence())
            .payoutInPence(payoutAmount)
            .freeSpinsAwarded(freeSpins)
            .build();
    }

    @Override
    public SpinOutcome supports() {
        return SpinOutcome.SMALL_PRIZE;
    }
}
