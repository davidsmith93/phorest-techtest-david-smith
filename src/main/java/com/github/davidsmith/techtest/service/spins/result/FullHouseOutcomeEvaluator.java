
package com.github.davidsmith.techtest.service.spins.result;

import com.github.davidsmith.techtest.model.SpinOutcome;
import com.github.davidsmith.techtest.remote.entity.MachineEntity;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Component
class FullHouseOutcomeEvaluator implements OutcomeEvaluator {
    @Override
    public SpinOutcome evaluate(MachineEntity machineEntity, List<String> slots) {
        final Set<String> machineColours = machineEntity.getColours();
        final int numberOfSlots = machineEntity.getNumberOfSlots();
        final Set<String> uniqueSlots = new HashSet<>(slots);

        if (machineColours.size() < numberOfSlots) {
            //B W
            //B B W W
            return uniqueSlots.size() == machineColours.size() && uniqueSlots.containsAll(machineColours) ?
                SpinOutcome.FULL_HOUSE :
                SpinOutcome.LOSS;
        } else if (machineColours.size() > numberOfSlots) {
            //B W G
            //B W
            return uniqueSlots.size() == numberOfSlots && machineColours.containsAll(uniqueSlots) ?
                SpinOutcome.FULL_HOUSE :
                SpinOutcome.LOSS;
        } else {
            //B W
            //B W
            return uniqueSlots.size() == numberOfSlots && uniqueSlots.containsAll(machineColours) ?
                SpinOutcome.FULL_HOUSE :
                SpinOutcome.LOSS;
        }
    }
}
