package com.github.davidsmith.techtest.service.spins.result;

import com.github.davidsmith.techtest.model.SpinOutcome;
import com.github.davidsmith.techtest.remote.entity.MachineEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class CompositeOutcomeEvaluator implements OutcomeEvaluator {

    private final JackpotOutcomeEvaluator jackpotOutcomeEvaluator;
    private final FullHouseOutcomeEvaluator fullHouseOutcomeEvaluator;
    private final SmallPrizeOutcomeEvaluator smallPrizeOutcomeEvaluator;

    @Override
    public SpinOutcome evaluate(MachineEntity machineEntity, List<String> slots) {
        SpinOutcome outcome = jackpotOutcomeEvaluator.evaluate(machineEntity, slots);
        if(outcome != SpinOutcome.LOSS) {
            return outcome;
        }

        outcome = fullHouseOutcomeEvaluator.evaluate(machineEntity, slots);
        if(outcome != SpinOutcome.LOSS) {
            return outcome;
        }

        return smallPrizeOutcomeEvaluator.evaluate(machineEntity, slots);
    }
}
