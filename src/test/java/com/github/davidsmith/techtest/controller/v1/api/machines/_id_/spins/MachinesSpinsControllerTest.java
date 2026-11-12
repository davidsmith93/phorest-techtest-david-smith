package com.github.davidsmith.techtest.controller.v1.api.machines._id_.spins;

import com.github.davidsmith.techtest.controller.v1.api.machines._id_.spins.response.MachineSpinsResult;
import com.github.davidsmith.techtest.service.spins.SpinService;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

@ExtendWith(MockitoExtension.class)
class MachinesSpinsControllerTest {

    @Mock
    private SpinService spinService;

    @InjectMocks
    private MachinesSpinsController machinesSpinsController;

    @Nested
    class Crete {
        @Test
        void delegatesToUnderlyingService() {
            machinesSpinsController.create();

            Mockito.verify(spinService).create();
        }

        @Test
        void returnsResultFromService() {
            final MachineSpinsResult result = Mockito.mock();

            Mockito.doReturn(result)
                .when(spinService)
                .create();

            Assertions.assertThat(machinesSpinsController.create())
                .returns(HttpStatus.OK, ResponseEntity::getStatusCode)
                .returns(result, HttpEntity::getBody);
        }
    }

}