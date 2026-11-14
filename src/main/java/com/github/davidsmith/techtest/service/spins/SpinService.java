package com.github.davidsmith.techtest.service.spins;

import com.github.davidsmith.techtest.controller.v1.api.machines._id_.spins.response.MachineSpinsResult;
import com.github.davidsmith.techtest.exception.EmptyMachineException;
import com.github.davidsmith.techtest.exception.MachineNotFoundException;
import com.github.davidsmith.techtest.model.SpinContext;
import com.github.davidsmith.techtest.model.SpinOutcome;
import com.github.davidsmith.techtest.model.SpinOutcomeResult;
import com.github.davidsmith.techtest.remote.entity.MachineEntity;
import com.github.davidsmith.techtest.remote.entity.UserEntity;
import com.github.davidsmith.techtest.remote.entity.UserSpinsEntity;
import com.github.davidsmith.techtest.remote.repository.MachineRepository;
import com.github.davidsmith.techtest.remote.repository.UserRepository;
import com.github.davidsmith.techtest.remote.repository.UserSpinsRepository;
import com.github.davidsmith.techtest.service.spins.outcome.OutcomeStrategyDelegator;
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
    private final UserRepository userRepository;
    private final UserSpinsRepository userSpinsRepository;
    private final SlotSpinnerFactory slotSpinnerFactory;
    private final OutcomeEvaluator compositeOutcomeEvaluator;
    private final OutcomeStrategyDelegator outcomeStrategyDelegator;

    @Transactional
    public MachineSpinsResult create(Integer machineId, Integer userId) {
        log.info("Starting spin");

        final MachineEntity machine = retrieveMachineEntity(machineId);
        final UserEntity user = retrieveUser(userId);
        final UserSpinsEntity userSpins = retrieveUserSpins(machineId, userId);

        validateUserAndMachine(machine, userSpins);
        processBuyIn(machine, userSpins);

        final SlotSpinner slotSpinner = slotSpinnerFactory.create(machine);

        final List<String> slots = slotSpinner.spin();
        final SpinOutcome outcome = compositeOutcomeEvaluator.evaluate(machine, slots);
        final SpinOutcomeResult result = outcomeStrategyDelegator.delegate(
            SpinContext.builder()
                .spinOutcome(outcome)
                .machine(machine)
                .userSpins(userSpins)
                .build()
        );

        userRepository.save(user);
        machineRepository.save(machine);
        userSpinsRepository.save(userSpins);

        log.info("Finished spin: slots {}, outcome: {}", slots, outcome);

        return MachineSpinsResult.builder()
            .slots(slots)
            .outcome(outcome.name())
            .freeSpinsAwarded(result.getFreeSpinsAwarded())
            .payoutInPence(result.getPayoutInPence())
            .remainingFloatInPence(result.getRemainingFloatInPence())
            .build();
    }

    private MachineEntity retrieveMachineEntity(Integer machineId) {
        log.debug("Attempting to retrieve fruit_machine with id {}", machineId);

        return machineRepository.findByIdForUpdate(machineId)
            .orElseThrow(() -> new MachineNotFoundException(
                "Unable to spin",
                "Machine could not be found with id %d".formatted(machineId)
            ));
    }

    private UserEntity retrieveUser(Integer userId) {
        log.debug("Attempting to retrieve users with id {}", userId);

        return userRepository.findById(userId)
            .orElseGet(() -> {
                final UserEntity user = UserEntity.builder()
                    .id(userId)
                    .build();

                user.setNewEntity(true);
                return user;
            });
    }

    private UserSpinsEntity retrieveUserSpins(Integer machineId, Integer userId) {
        log.debug("Attempting to retrieve user_spins for user_id {} and machine_id {}", userId, machineId);

        final UserSpinsEntity.Identifier identifier = UserSpinsEntity.Identifier.builder()
            .fruitMachineId(machineId)
            .userId(userId)
            .build();

        return userSpinsRepository.findById(identifier)
            .orElseGet(() -> {
                final UserSpinsEntity userSpins = UserSpinsEntity.builder()
                    .id(identifier)
                    .freeSpins(0)
                    .build();

                userSpins.setNewEntity(true);
                return userSpins;
            });
    }

    private void validateUserAndMachine(MachineEntity machine, UserSpinsEntity userSpins) {
        if(machine.getFloatInPence() == 0 && userSpins.getFreeSpins() > 0) {
            throw new EmptyMachineException(
                "Unable to spin",
                "Cannot process a free spin when the machine is empty, no credit has been used"
            );
        }
    }

    private void processBuyIn(MachineEntity machineEntity, UserSpinsEntity userSpins) {
        if(userSpins.getFreeSpins() > 0) {
            log.debug("User has free spins, redeeming one now");
            userSpins.setFreeSpins(userSpins.getFreeSpins() - 1);
        } else {
            machineEntity.setFloatInPence(machineEntity.getFloatInPence() + machineEntity.getSpinPriceInPence());
        }
    }
}
