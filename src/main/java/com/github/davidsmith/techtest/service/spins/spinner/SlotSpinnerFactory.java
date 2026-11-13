package com.github.davidsmith.techtest.service.spins.spinner;

import com.github.davidsmith.techtest.exception.BadMachineException;
import com.github.davidsmith.techtest.remote.entity.MachineEntity;
import org.springframework.stereotype.Component;

import java.security.SecureRandom;
import java.util.ArrayList;

@Component
public class SlotSpinnerFactory {
    public SlotSpinner create(MachineEntity machine) {
        return switch (machine.getSpinType()) {
            case null -> throw new BadMachineException("Unsupported machine", "Machine must be of type RANDOM");
            case RANDOM -> new RandomSlotSpinner(
                new ArrayList<>(machine.getColours()),
                machine.getNumberOfSlots(),
                new SecureRandom()
            );
        };
    }
}
