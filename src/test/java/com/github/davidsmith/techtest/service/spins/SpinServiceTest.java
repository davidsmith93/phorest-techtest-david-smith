package com.github.davidsmith.techtest.service.spins;

import com.github.davidsmith.techtest.controller.v1.api.machines._id_.spins.response.MachineSpinsResult;
import com.github.davidsmith.techtest.model.SpinOutcome;
import com.github.davidsmith.techtest.remote.entity.MachineEntity;
import com.github.davidsmith.techtest.remote.repository.MachineRepository;
import com.github.davidsmith.techtest.service.spins.result.OutcomeEvaluator;
import com.github.davidsmith.techtest.service.spins.spinner.SlotSpinner;
import com.github.davidsmith.techtest.service.spins.spinner.SlotSpinnerFactory;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

@ExtendWith(MockitoExtension.class)
class SpinServiceTest {

    @Mock
    MachineRepository machineRepository;

    @Mock
    SlotSpinnerFactory slotSpinnerFactory;

    @Mock
    OutcomeEvaluator outcomeEvaluator;

    @InjectMocks
    SpinService spinService;

    private static final Integer MACHINE_ID = 1;

    @Nested
    class Create {
        @Test
        void returnsExpectedResult() {
            final MachineEntity machine = Mockito.mock();
            final SlotSpinner slotSpinner = Mockito.mock();

            final List<String> slots = Mockito.mock();
            final SpinOutcome outcome = SpinOutcome.JACKPOT;

            Mockito.doReturn(Optional.of(machine))
                .when(machineRepository)
                .findById(MACHINE_ID);

            Mockito.doReturn(slotSpinner)
                .when(slotSpinnerFactory)
                .create(machine);

            Mockito.doReturn(slots)
                .when(slotSpinner)
                .spin();

            Mockito.doReturn(outcome)
                .when(outcomeEvaluator)
                .evaluate(machine, slots);

            Assertions.assertThat(spinService.create(MACHINE_ID))
                .returns(slots, MachineSpinsResult::getSlots)
                .returns(outcome.name(), MachineSpinsResult::getOutcome);
        }
    }

}