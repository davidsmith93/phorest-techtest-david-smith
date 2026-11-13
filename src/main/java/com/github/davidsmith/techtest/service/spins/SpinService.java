package com.github.davidsmith.techtest.service.spins;

import com.github.davidsmith.techtest.controller.v1.api.machines._id_.spins.response.MachineSpinsResult;
import com.github.davidsmith.techtest.exception.MachineNotFoundException;
import com.github.davidsmith.techtest.model.SpinOutcome;
import com.github.davidsmith.techtest.remote.entity.MachineEntity;
import com.github.davidsmith.techtest.remote.repository.MachineRepository;
import com.github.davidsmith.techtest.service.spins.result.OutcomeEvaluator;
import com.github.davidsmith.techtest.service.spins.spinner.SlotSpinner;
import com.github.davidsmith.techtest.service.spins.spinner.SlotSpinnerFactory;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class SpinService {

    private final MachineRepository machineRepository;
    private final SlotSpinnerFactory slotSpinnerFactory;
    private final OutcomeEvaluator compositeOutcomeEvaluator;

    @Transactional
    public MachineSpinsResult create(Integer machineId) {
        log.info("Starting spin");

        final MachineEntity machine = retrieveMachineEntity(machineId);

        final SlotSpinner slotSpinner = slotSpinnerFactory.create(machine);

        final List<String> slots = slotSpinner.spin();
        final SpinOutcome outcome = compositeOutcomeEvaluator.evaluate(machine, slots);

        log.info("Finished spin: slots {}, outcome: {}", slots, outcome);

        return MachineSpinsResult.builder()
            .slots(slots)
            .outcome(outcome.name())
            .build();
    }

    public MachineEntity retrieveMachineEntity(Integer machineId) {
        return machineRepository.findById(machineId)
            .orElseThrow(() -> new MachineNotFoundException(
                "Unable to spin",
                "Machine could not be found with id %d".formatted(machineId)
            ));
    }
}
