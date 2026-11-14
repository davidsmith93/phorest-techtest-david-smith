package com.github.davidsmith.techtest.service.spins.outcome;

import com.github.davidsmith.techtest.model.SpinContext;
import com.github.davidsmith.techtest.model.SpinOutcome;
import com.github.davidsmith.techtest.model.SpinOutcomeResult;
import com.github.davidsmith.techtest.remote.entity.MachineEntity;
import com.github.davidsmith.techtest.remote.repository.MachineRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
public class LossOutcomeStrategy implements OutcomeStrategy {

    @Override
    public SpinOutcomeResult resolve(SpinContext spinContext) {
        final MachineEntity machine = spinContext.getMachine();

        return SpinOutcomeResult.builder()
            .remainingFloatInPence(machine.getFloatInPence())
            .build();
    }

    @Override
    public SpinOutcome supports() {
        return SpinOutcome.LOSS;
    }
}
