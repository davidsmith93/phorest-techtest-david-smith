package com.github.davidsmith.techtest.service.spins.result;

import com.github.davidsmith.techtest.model.SpinOutcome;
import com.github.davidsmith.techtest.remote.entity.MachineEntity;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.List;

@Component
class JackpotOutcomeEvaluator implements OutcomeEvaluator {
    @Override
    public SpinOutcome evaluate(MachineEntity machineEntity, List<String> slots) {
        return new HashSet<>(slots).size() == 1 ? SpinOutcome.JACKPOT : SpinOutcome.LOSS;
    }
}
