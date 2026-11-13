package com.github.davidsmith.techtest.service.spins.result;

import com.github.davidsmith.techtest.model.SpinOutcome;
import com.github.davidsmith.techtest.remote.entity.MachineEntity;

import java.util.List;

public interface OutcomeEvaluator {
    SpinOutcome evaluate(MachineEntity machineEntity, List<String> slots);
}
