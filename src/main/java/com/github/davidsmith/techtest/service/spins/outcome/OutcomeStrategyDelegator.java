package com.github.davidsmith.techtest.service.spins.outcome;

import com.github.davidsmith.techtest.model.SpinContext;
import com.github.davidsmith.techtest.model.SpinOutcome;
import com.github.davidsmith.techtest.model.SpinOutcomeResult;
import org.springframework.stereotype.Component;
import com.github.davidsmith.techtest.exception.StrategyNotFoundException;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class OutcomeStrategyDelegator {

    private final Map<SpinOutcome, OutcomeStrategy> outcomeStrategies = new HashMap<>();

    public OutcomeStrategyDelegator(List<OutcomeStrategy> outcomeStrategies) {
        outcomeStrategies.forEach(outcomeStrategy ->
            this.outcomeStrategies.put(outcomeStrategy.supports(), outcomeStrategy
        ));
    }

    public SpinOutcomeResult delegate(SpinContext spinContext) {
        final OutcomeStrategy outcomeStrategy = outcomeStrategies.get(spinContext.getSpinOutcome());

        if(outcomeStrategy == null) {
            throw new StrategyNotFoundException(
                "No payout strategy exists for %s outcome".formatted(spinContext.getSpinOutcome())
            );
        }

        return outcomeStrategy.resolve(spinContext);
    }

}
