package com.github.davidsmith.techtest.controller.v1.api.machines;

import com.github.davidsmith.techtest.controller.v1.api.machines.request.CreateMachineRequest;
import com.github.davidsmith.techtest.controller.v1.api.machines.request.UpdateMachineRequest;
import com.github.davidsmith.techtest.controller.v1.api.machines.response.CreateMachineResponse;
import com.github.davidsmith.techtest.controller.v1.api.machines.response.RetrieveMachineResponse;
import com.github.davidsmith.techtest.service.machines.MachinesService;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

@ExtendWith(MockitoExtension.class)
class MachinesControllerTest {

    @Mock
    MachinesService machinesService;

    @InjectMocks
    MachinesController machinesController;

    @Nested
    class Create {
        @Test
        void returnsExpected() {
            CreateMachineRequest request = Mockito.mock();
            CreateMachineResponse response = Mockito.mock();

            Mockito.doReturn(response).when(machinesService).create(request);

            Assertions.assertThat(machinesController.create(request))
                .returns(HttpStatus.CREATED, ResponseEntity::getStatusCode)
                .returns(response, ResponseEntity::getBody);
        }
    }

    @Nested
    class Retrieve {
        @Test
        void returnsExpected() {
            Integer machineId = 1;
            RetrieveMachineResponse response = Mockito.mock();

            Mockito.doReturn(response).when(machinesService).retrieve(machineId);

            Assertions.assertThat(machinesController.retrieve(machineId))
                .returns(HttpStatus.OK, ResponseEntity::getStatusCode)
                .returns(response, ResponseEntity::getBody);
        }
    }

    @Nested
    class Update {
        @Test
        void delegatesCorrectly() {
            Integer machineId = 1;
            UpdateMachineRequest request = Mockito.mock();

            Assertions.assertThat(machinesController.update(request, machineId))
                .returns(HttpStatus.NO_CONTENT, ResponseEntity::getStatusCode);

            Mockito.verify(machinesService).update(machineId, request);
        }
    }

}