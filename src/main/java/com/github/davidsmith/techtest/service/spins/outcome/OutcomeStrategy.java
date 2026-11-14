package com.github.davidsmith.techtest.service.spins.outcome;

import com.github.davidsmith.techtest.model.SpinContext;
import com.github.davidsmith.techtest.model.SpinOutcome;
import com.github.davidsmith.techtest.model.SpinOutcomeResult;

public interface OutcomeStrategy {
    SpinOutcomeResult resolve(SpinContext spinContext);
    SpinOutcome supports();
}
