package com.github.davidsmith.techtest.service.spins.result;

import com.github.davidsmith.techtest.model.SpinOutcome;

import java.util.List;

public interface OutcomeEvaluator {
    SpinOutcome evaluate(List<String> slots);
}
