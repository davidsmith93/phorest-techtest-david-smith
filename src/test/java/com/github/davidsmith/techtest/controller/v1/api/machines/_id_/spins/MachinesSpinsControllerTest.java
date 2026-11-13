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

    private static final Integer MACHINE_ID = 1;
    private static final Integer USER_ID = 2;

    @Nested
    class Crete {
        @Test
        void delegatesToUnderlyingService() {
            machinesSpinsController.create(MACHINE_ID, USER_ID);

            Mockito.verify(spinService).create(MACHINE_ID, USER_ID);
        }

        @Test
        void returnsResultFromService() {
            final MachineSpinsResult result = Mockito.mock();

            Mockito.doReturn(result)
                .when(spinService)
                .create(MACHINE_ID, USER_ID);

            Assertions.assertThat(machinesSpinsController.create(MACHINE_ID, USER_ID))
                .returns(HttpStatus.OK, ResponseEntity::getStatusCode)
                .returns(result, HttpEntity::getBody);
        }
    }

}