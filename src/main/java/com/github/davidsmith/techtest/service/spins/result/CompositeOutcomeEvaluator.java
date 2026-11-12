package com.github.davidsmith.techtest.service.spins.result;

import com.github.davidsmith.techtest.model.SpinOutcome;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class CompositeOutcomeEvaluator implements OutcomeEvaluator {

    private final JackpotOutcomeEvaluator jackpotOutcomeEvaluator;

    @Override
    public SpinOutcome evaluate(List<String> slots) {
        return jackpotOutcomeEvaluator.evaluate(slots);
    }
}
