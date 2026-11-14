package com.github.davidsmith.techtest.service.machines;

import com.github.davidsmith.techtest.controller.v1.api.machines.request.CreateMachineRequest;
import com.github.davidsmith.techtest.controller.v1.api.machines.request.UpdateMachineRequest;
import com.github.davidsmith.techtest.controller.v1.api.machines.response.CreateMachineResponse;
import com.github.davidsmith.techtest.controller.v1.api.machines.response.RetrieveMachineResponse;
import com.github.davidsmith.techtest.exception.MachineNotFoundException;
import com.github.davidsmith.techtest.exception.ValidationException;
import com.github.davidsmith.techtest.exception.base.BadRequestException;
import com.github.davidsmith.techtest.remote.entity.MachineEntity;
import com.github.davidsmith.techtest.remote.repository.MachineRepository;
import org.assertj.core.api.Assertions;
import org.assertj.core.api.InstanceOfAssertFactories;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;
import java.util.Set;

@ExtendWith(MockitoExtension.class)
class MachinesServiceTest {

    @Mock
    MachineRepository machineRepository;

    @InjectMocks
    MachinesService machinesService;

    @Nested
    class Create {
        @Test
        void throwsValidationException_whenColoursLessThan2() {
            final CreateMachineRequest request = CreateMachineRequest.builder()
                .colours(Set.of(
                    "BLACK",
                    "black",
                    "black  ",
                    " "
                ))
                .build();

            Assertions.assertThatThrownBy(() -> machinesService.create(request))
                .isExactlyInstanceOf(ValidationException.class)
                .asInstanceOf(InstanceOfAssertFactories.type(ValidationException.class))
                .returns("Colours was fewer than minimum of 2", BadRequestException::getError);
        }

        @Test
        void throwsValidationException_whenSmallPrizeAdjacencyGreaterThanNumberOfSlots() {
            final CreateMachineRequest request = CreateMachineRequest.builder()
                .colours(Set.of("BLACK", "WHITE"))
                .numberOfSlots(4)
                .smallPrizeAdjacency(5)
                .build();

            Assertions.assertThatThrownBy(() -> machinesService.create(request))
                .isExactlyInstanceOf(ValidationException.class)
                .asInstanceOf(InstanceOfAssertFactories.type(ValidationException.class))
                .returns("Small prize adjacency was greater than the number of slots", BadRequestException::getError);
        }

        @Test
        void savesNewMachine_whenValid() {
            final CreateMachineRequest request = CreateMachineRequest.builder()
                .colours(Set.of("BLACK", "WHITE", "black"))
                .numberOfSlots(2)
                .smallPrizeAdjacency(2)
                .floatInPence(1000)
                .spinPriceInPence(10)
                .build();

            final MachineEntity createdMachine = MachineEntity.builder()
                .id(1)
                .build();

            Mockito.doReturn(createdMachine).when(machineRepository).save(Mockito.any());

            Assertions.assertThat(machinesService.create(request))
                .returns(1, CreateMachineResponse::getId);

            final ArgumentCaptor<MachineEntity> captor = ArgumentCaptor.forClass(MachineEntity.class);
            Mockito.verify(machineRepository).save(captor.capture());

            Assertions.assertThat(captor.getValue())
                .returns(Set.of("BLACK", "WHITE"), MachineEntity::getColours)
                .returns(2, MachineEntity::getNumberOfSlots)
                .returns(2, MachineEntity::getSmallPrizeAdjacency)
                .returns(1000, MachineEntity::getFloatInPence)
                .returns(10, MachineEntity::getSpinPriceInPence)
                .returns(MachineEntity.SpinType.RANDOM, MachineEntity::getSpinType);
        }
    }

    @Nested
    class Retrieve {
        private static final int MACHINE_ID = 1;

        @Test
        void throwsMachineNotFoundException_whenNoMachineFound() {
            Assertions.assertThatThrownBy(() -> machinesService.retrieve(MACHINE_ID))
                .isExactlyInstanceOf(MachineNotFoundException.class);
        }

        @Test
        void returnsExpected_whenValid() {
            final MachineEntity machineEntity = MachineEntity.builder()
                .id(1)
                .colours(Set.of("BLACK", "WHITE"))
                .numberOfSlots(2)
                .smallPrizeAdjacency(2)
                .floatInPence(1000)
                .spinPriceInPence(10)
                .build();

            Mockito.doReturn(Optional.of(machineEntity)).when(machineRepository).findById(MACHINE_ID);

            Assertions.assertThat(machinesService.retrieve(MACHINE_ID))
                .returns(1, RetrieveMachineResponse::getId)
                .returns(Set.of("BLACK", "WHITE"), RetrieveMachineResponse::getColours)
                .returns(2, RetrieveMachineResponse::getNumberOfSlots)
                .returns(2, RetrieveMachineResponse::getSmallPrizeAdjacency)
                .returns(1000, RetrieveMachineResponse::getFloatInPence)
                .returns(10, RetrieveMachineResponse::getSpinPriceInPence);
        }
    }

    @Nested
    class Update {

        private static final int MACHINE_ID = 1;

        @Test
        void throwsMachineNotFoundException_whenNoMachineFound() {
            Assertions.assertThatThrownBy(() -> machinesService.update(MACHINE_ID, null))
                .isExactlyInstanceOf(MachineNotFoundException.class);
        }

        @Test
        void throwsValidationException_whenColoursLessThan2() {
            final MachineEntity machineEntity = MachineEntity.builder().build();
            Mockito.doReturn(Optional.of(machineEntity)).when(machineRepository).findByIdForUpdate(MACHINE_ID);

            final UpdateMachineRequest request = UpdateMachineRequest.builder()
                .colours(Set.of(
                    "BLACK",
                    "black",
                    "black  ",
                    " "
                ))
                .build();

            Assertions.assertThatThrownBy(() -> machinesService.update(MACHINE_ID, request))
                .isExactlyInstanceOf(ValidationException.class)
                .asInstanceOf(InstanceOfAssertFactories.type(ValidationException.class))
                .returns("Colours was fewer than minimum of 2", BadRequestException::getError);
        }

        @Test
        void throwsValidationException_whenSmallPrizeAdjacencyGreaterThanNumberOfSlots() {
            final MachineEntity machineEntity = MachineEntity.builder().build();
            Mockito.doReturn(Optional.of(machineEntity)).when(machineRepository).findByIdForUpdate(MACHINE_ID);

            final UpdateMachineRequest request = UpdateMachineRequest.builder()
                .colours(Set.of("BLACK", "WHITE"))
                .numberOfSlots(4)
                .smallPrizeAdjacency(5)
                .build();

            Assertions.assertThatThrownBy(() -> machinesService.update(MACHINE_ID, request))
                .isExactlyInstanceOf(ValidationException.class)
                .asInstanceOf(InstanceOfAssertFactories.type(ValidationException.class))
                .returns("Small prize adjacency was greater than the number of slots", BadRequestException::getError);
        }

        @Test
        void savesNewMachine_whenValid() {
            final UpdateMachineRequest request = UpdateMachineRequest.builder()
                .colours(Set.of("BLACK", "WHITE", "black"))
                .numberOfSlots(2)
                .smallPrizeAdjacency(2)
                .floatInPence(1000)
                .spinPriceInPence(10)
                .build();

            final MachineEntity createdMachine = MachineEntity.builder()
                .id(1)
                .build();

            Mockito.doReturn(Optional.of(createdMachine)).when(machineRepository).findByIdForUpdate(MACHINE_ID);

            machinesService.update(MACHINE_ID, request);

            final ArgumentCaptor<MachineEntity> captor = ArgumentCaptor.forClass(MachineEntity.class);
            Mockito.verify(machineRepository).save(captor.capture());

            Assertions.assertThat(captor.getValue())
                .returns(Set.of("BLACK", "WHITE"), MachineEntity::getColours)
                .returns(2, MachineEntity::getNumberOfSlots)
                .returns(2, MachineEntity::getSmallPrizeAdjacency)
                .returns(1000, MachineEntity::getFloatInPence)
                .returns(10, MachineEntity::getSpinPriceInPence);
        }
    }

}