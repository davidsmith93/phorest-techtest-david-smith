package com.github.davidsmith.techtest.service.spins;

import com.github.davidsmith.techtest.controller.v1.api.machines._id_.spins.response.MachineSpinsResult;
import com.github.davidsmith.techtest.exception.EmptyMachineException;
import com.github.davidsmith.techtest.exception.MachineNotFoundException;
import com.github.davidsmith.techtest.model.SpinOutcome;
import com.github.davidsmith.techtest.model.SpinOutcomeResult;
import com.github.davidsmith.techtest.remote.entity.MachineEntity;
import com.github.davidsmith.techtest.remote.entity.PersistableEntity;
import com.github.davidsmith.techtest.remote.entity.UserEntity;
import com.github.davidsmith.techtest.remote.entity.UserSpinsEntity;
import com.github.davidsmith.techtest.remote.repository.MachineRepository;
import com.github.davidsmith.techtest.remote.repository.UserRepository;
import com.github.davidsmith.techtest.remote.repository.UserSpinsRepository;
import com.github.davidsmith.techtest.service.spins.outcome.OutcomeStrategyDelegator;
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
    UserRepository userRepository;

    @Mock
    UserSpinsRepository userSpinsRepository;

    @Mock
    SlotSpinnerFactory slotSpinnerFactory;

    @Mock
    OutcomeEvaluator outcomeEvaluator;

    @Mock
    OutcomeStrategyDelegator outcomeStrategyDelegator;

    @InjectMocks
    SpinService spinService;

    private static final Integer MACHINE_ID = 1;
    private static final Integer USER_ID = 2;

    @Nested
    class Create {
        @Test
        void throwsException_whenMachineNotFound() {
            Assertions.assertThatThrownBy(() -> spinService.create(MACHINE_ID, USER_ID))
                .isExactlyInstanceOf(MachineNotFoundException.class);
        }

        @Test
        void throwsException_whenMachineIsEmptyAndUserHasFreeSpins() {
            final MachineEntity machine = Mockito.mock();
            final UserSpinsEntity userSpins = Mockito.mock();

            Mockito.doReturn(0).when(machine).getFloatInPence();
            Mockito.doReturn(1).when(userSpins).getFreeSpins();
            Mockito.doReturn(Optional.of(machine)).when(machineRepository).findByIdForUpdate(MACHINE_ID);
            Mockito.doReturn(Optional.of(userSpins))
                .when(userSpinsRepository)
                .findById(new UserSpinsEntity.Identifier(USER_ID, MACHINE_ID));

            Assertions.assertThatThrownBy(() -> spinService.create(MACHINE_ID, USER_ID))
                .isExactlyInstanceOf(EmptyMachineException.class);
        }

        @Test
        void returnsExpectedResult_whenUserHasNoFreeSpins() {
            final MachineEntity machine = Mockito.mock();
            final SlotSpinner slotSpinner = Mockito.mock();

            final List<String> slots = Mockito.mock();
            final SpinOutcome outcome = SpinOutcome.JACKPOT;
            final SpinOutcomeResult spinOutcomeResult = Mockito.mock();

            Mockito.doReturn(Optional.of(machine)).when(machineRepository).findByIdForUpdate(MACHINE_ID);
            Mockito.doReturn(slotSpinner).when(slotSpinnerFactory).create(machine);
            Mockito.doReturn(slots).when(slotSpinner).spin();
            Mockito.doReturn(outcome).when(outcomeEvaluator).evaluate(machine, slots);
            Mockito.doReturn(spinOutcomeResult).when(outcomeStrategyDelegator).delegate(Mockito.any());
            Mockito.doReturn(1000).when(spinOutcomeResult).getPayoutInPence();
            Mockito.doReturn(500).when(spinOutcomeResult).getRemainingFloatInPence();
            Mockito.doReturn(10).when(spinOutcomeResult).getFreeSpinsAwarded();

            Assertions.assertThat(spinService.create(MACHINE_ID, USER_ID))
                .returns(slots, MachineSpinsResult::getSlots)
                .returns(outcome.name(), MachineSpinsResult::getOutcome)
                .returns(1000, MachineSpinsResult::getPayoutInPence)
                .returns(500, MachineSpinsResult::getRemainingFloatInPence)
                .returns(10, MachineSpinsResult::getFreeSpinsAwarded);
        }

        @Test
        void returnsExpectedResult_whenUserHasFreeSpins() {
            final MachineEntity machine = Mockito.mock();
            final UserSpinsEntity userSpins = Mockito.mock();
            final SlotSpinner slotSpinner = Mockito.mock();

            final List<String> slots = Mockito.mock();
            final SpinOutcome outcome = SpinOutcome.JACKPOT;
            final SpinOutcomeResult spinOutcomeResult = Mockito.mock();

            Mockito.doReturn(10000).when(machine).getFloatInPence();
            Mockito.doReturn(1).when(userSpins).getFreeSpins();
            Mockito.doReturn(Optional.of(machine)).when(machineRepository).findByIdForUpdate(MACHINE_ID);
            Mockito.doReturn(Optional.of(userSpins))
                .when(userSpinsRepository)
                .findById(new UserSpinsEntity.Identifier(USER_ID, MACHINE_ID));
            Mockito.doReturn(slotSpinner).when(slotSpinnerFactory).create(machine);
            Mockito.doReturn(slots).when(slotSpinner).spin();
            Mockito.doReturn(outcome).when(outcomeEvaluator).evaluate(machine, slots);
            Mockito.doReturn(spinOutcomeResult).when(outcomeStrategyDelegator).delegate(Mockito.any());
            Mockito.doReturn(1000).when(spinOutcomeResult).getPayoutInPence();
            Mockito.doReturn(500).when(spinOutcomeResult).getRemainingFloatInPence();
            Mockito.doReturn(10).when(spinOutcomeResult).getFreeSpinsAwarded();

            Assertions.assertThat(spinService.create(MACHINE_ID, USER_ID))
                .returns(slots, MachineSpinsResult::getSlots)
                .returns(outcome.name(), MachineSpinsResult::getOutcome)
                .returns(1000, MachineSpinsResult::getPayoutInPence)
                .returns(500, MachineSpinsResult::getRemainingFloatInPence)
                .returns(10, MachineSpinsResult::getFreeSpinsAwarded);
        }

        @Test
        void savesExpectedRecords_whenNoUserData() {
            final MachineEntity machine = Mockito.mock();
            final SlotSpinner slotSpinner = Mockito.mock();

            final List<String> slots = Mockito.mock();
            final SpinOutcome outcome = SpinOutcome.JACKPOT;
            final SpinOutcomeResult spinOutcomeResult = Mockito.mock();

            Mockito.doReturn(Optional.of(machine)).when(machineRepository).findByIdForUpdate(MACHINE_ID);
            Mockito.doReturn(slotSpinner).when(slotSpinnerFactory).create(machine);
            Mockito.doReturn(slots).when(slotSpinner).spin();
            Mockito.doReturn(outcome).when(outcomeEvaluator).evaluate(machine, slots);
            Mockito.doReturn(spinOutcomeResult).when(outcomeStrategyDelegator).delegate(Mockito.any());

            spinService.create(MACHINE_ID, USER_ID);

            Mockito.verify(machineRepository).save(machine);
            Mockito.verify(userRepository).save(Mockito.argThat(PersistableEntity::isNew));
            Mockito.verify(userSpinsRepository).save(Mockito.argThat(PersistableEntity::isNew));
        }

        @Test
        void savesExpectedRecords_whenExistingUserDataPresent() {
            final MachineEntity machine = Mockito.mock();
            final UserEntity user = Mockito.mock();
            final UserSpinsEntity userSpins = Mockito.mock();
            final SlotSpinner slotSpinner = Mockito.mock();

            final List<String> slots = Mockito.mock();
            final SpinOutcome outcome = SpinOutcome.JACKPOT;
            final SpinOutcomeResult spinOutcomeResult = Mockito.mock();

            Mockito.doReturn(Optional.of(machine)).when(machineRepository).findByIdForUpdate(MACHINE_ID);
            Mockito.doReturn(Optional.of(user)).when(userRepository).findById(USER_ID);
            Mockito.doReturn(Optional.of(userSpins))
                .when(userSpinsRepository)
                .findById(new UserSpinsEntity.Identifier(USER_ID, MACHINE_ID));
            Mockito.doReturn(slotSpinner).when(slotSpinnerFactory).create(machine);
            Mockito.doReturn(slots).when(slotSpinner).spin();
            Mockito.doReturn(outcome).when(outcomeEvaluator).evaluate(machine, slots);
            Mockito.doReturn(spinOutcomeResult).when(outcomeStrategyDelegator).delegate(Mockito.any());

            spinService.create(MACHINE_ID, USER_ID);

            Mockito.verify(machineRepository).save(machine);
            Mockito.verify(userRepository).save(user);
            Mockito.verify(userSpinsRepository).save(userSpins);

            Mockito.verify(user, Mockito.never()).setNewEntity(true);
            Mockito.verify(userSpins, Mockito.never()).setNewEntity(true);
        }
    }

}