
package com.github.davidsmith.techtest.service.spins.result;

import com.github.davidsmith.techtest.model.SpinOutcome;
import com.github.davidsmith.techtest.remote.entity.MachineEntity;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.List;

@Component
class SmallPrizeOutcomeEvaluator implements OutcomeEvaluator {
    @Override
    public SpinOutcome evaluate(MachineEntity machineEntity, List<String> slots) {
        for(int i = 0; i <= slots.size() - machineEntity.getSmallPrizeAdjacency(); i++) {
            if(new HashSet<>(slots.subList(i, i + machineEntity.getSmallPrizeAdjacency())).size() == 1) {
                return SpinOutcome.SMALL_PRIZE;
            }
        }

        return SpinOutcome.LOSS;
    }
}
