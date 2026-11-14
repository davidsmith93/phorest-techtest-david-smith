package com.github.davidsmith.techtest.service.machines;

import com.github.davidsmith.techtest.controller.v1.api.machines.request.CreateMachineRequest;
import com.github.davidsmith.techtest.controller.v1.api.machines.request.UpdateMachineRequest;
import com.github.davidsmith.techtest.controller.v1.api.machines.response.CreateMachineResponse;
import com.github.davidsmith.techtest.controller.v1.api.machines.response.RetrieveMachineResponse;
import com.github.davidsmith.techtest.exception.MachineNotFoundException;
import com.github.davidsmith.techtest.exception.ValidationException;
import com.github.davidsmith.techtest.remote.entity.MachineEntity;
import com.github.davidsmith.techtest.remote.repository.MachineRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.function.Supplier;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class MachinesService {

    private final MachineRepository machineRepository;

    @Transactional
    public CreateMachineResponse create(CreateMachineRequest request) {
        final MachineEntity newMachine = MachineEntity.builder()
            .colours(formatColours(request.getColours()))
            .numberOfSlots(request.getNumberOfSlots())
            .spinType(MachineEntity.SpinType.RANDOM)
            .floatInPence(request.getFloatInPence())
            .spinPriceInPence(request.getSpinPriceInPence())
            .smallPrizeAdjacency(request.getSmallPrizeAdjacency())
            .build();

        newMachine.setNewEntity(true);

        validateMachine(newMachine);

        final MachineEntity savedMachine = machineRepository.save(newMachine);
        return CreateMachineResponse.builder()
            .id(savedMachine.getId())
            .build();
    }

    @Transactional(readOnly = true)
    public RetrieveMachineResponse retrieve(Integer machineId) {
        final MachineEntity machine = load(machineId, () -> machineRepository.findById(machineId));

        return RetrieveMachineResponse.builder()
            .id(machine.getId())
            .colours(machine.getColours())
            .numberOfSlots(machine.getNumberOfSlots())
            .floatInPence(machine.getFloatInPence())
            .spinPriceInPence(machine.getSpinPriceInPence())
            .smallPrizeAdjacency(machine.getSmallPrizeAdjacency())
            .build();
    }

    @Transactional
    public void update(Integer machineId, UpdateMachineRequest request) {
        final MachineEntity machine = load(machineId, () -> machineRepository.findByIdForUpdate(machineId));
        machine.setColours(formatColours(request.getColours()));
        machine.setNumberOfSlots(request.getNumberOfSlots());
        machine.setFloatInPence(request.getFloatInPence());
        machine.setSpinPriceInPence(request.getSpinPriceInPence());
        machine.setSmallPrizeAdjacency(request.getSmallPrizeAdjacency());

        validateMachine(machine);

        machineRepository.save(machine);
    }

    private MachineEntity load(Integer machineId, Supplier<Optional<MachineEntity>> supplier) {
        return supplier.get()
            .orElseThrow(() -> new MachineNotFoundException(
                "Unable to find machine",
                "Machine could not be found with id %d".formatted(machineId)
            ));
    }

    private void validateMachine(MachineEntity machineEntity) {
        if(machineEntity.getColours().size() < 2) {
            throw new ValidationException(
                "Invalid configuration",
                "Colours was fewer than minimum of 2"
            );
        }

        if(machineEntity.getSmallPrizeAdjacency() > machineEntity.getNumberOfSlots()) {
            throw new ValidationException(
                "Invalid configuration",
                "Small prize adjacency was greater than the number of slots"
            );
        }
    }

    private Set<String> formatColours(Set<String> colours) {
        return colours.stream()
            .filter(Objects::nonNull)
            .map(String::trim)
            .filter(colour -> !colour.isEmpty())
            .map(String::toUpperCase)
            .collect(Collectors.toSet());
    }

}
