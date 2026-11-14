package com.github.davidsmith.techtest.controller.v1.api.machines._id_.spins;

import com.github.davidsmith.techtest.remote.entity.MachineEntity;
import com.github.davidsmith.techtest.remote.repository.MachineRepository;
import com.github.davidsmith.techtest.service.spins.spinner.SlotSpinner;
import com.github.davidsmith.techtest.service.spins.spinner.SlotSpinnerFactory;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.List;
import java.util.Set;

@SpringBootTest
@AutoConfigureMockMvc
public class MachinesSpinsIntegrationTest {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    MachineRepository machineRepository;

    @MockitoBean
    SlotSpinnerFactory slotSpinnerFactory;

    @Nested
    class Create {
        @Test
        void returnsExpectedResult_whenJackpot() throws Exception {
            final MachineEntity machine = createMachine();
            final SlotSpinner slotSpinner = Mockito.mock();

            Mockito.doReturn(slotSpinner)
                .when(slotSpinnerFactory)
                .create(Mockito.any());

            Mockito.doReturn(List.of("BLACK", "BLACK", "BLACK", "BLACK"))
                .when(slotSpinner)
                .spin();

            mockMvc.perform(createRequest(machine.getId()))
                .andExpectAll(
                    MockMvcResultMatchers.status().is(200),
                    MockMvcResultMatchers.jsonPath("$.slots", Matchers.equalTo(List.of("BLACK", "BLACK", "BLACK", "BLACK"))),
                    MockMvcResultMatchers.jsonPath("$.outcome", Matchers.equalTo("JACKPOT")),
                    MockMvcResultMatchers.jsonPath("$.payoutInPence", Matchers.equalTo(10100)),
                    MockMvcResultMatchers.jsonPath("$.remainingFloatInPence", Matchers.equalTo(0)),
                    MockMvcResultMatchers.jsonPath("$.freeSpinsAwarded", Matchers.equalTo(null))
                );
        }

        @Test
        void returnsExpectedResult_whenFullHouse() throws Exception {
            final MachineEntity machine = createMachine();
            final SlotSpinner slotSpinner = Mockito.mock();

            Mockito.doReturn(slotSpinner)
                .when(slotSpinnerFactory)
                .create(Mockito.any());

            Mockito.doReturn(List.of("BLACK", "GREEN", "YELLOW", "WHITE"))
                .when(slotSpinner)
                .spin();

            mockMvc.perform(createRequest(machine.getId()))
                .andExpectAll(
                    MockMvcResultMatchers.status().is(200),
                    MockMvcResultMatchers.jsonPath("$.slots", Matchers.equalTo(List.of("BLACK", "GREEN", "YELLOW", "WHITE"))),
                    MockMvcResultMatchers.jsonPath("$.outcome", Matchers.equalTo("FULL_HOUSE")),
                    MockMvcResultMatchers.jsonPath("$.payoutInPence", Matchers.equalTo(5050)),
                    MockMvcResultMatchers.jsonPath("$.remainingFloatInPence", Matchers.equalTo(5050)),
                    MockMvcResultMatchers.jsonPath("$.freeSpinsAwarded", Matchers.equalTo(null))
                );
        }

        @Test
        void returnsExpectedResult_whenSmallPrize() throws Exception {
            final MachineEntity machine = createMachine();
            final SlotSpinner slotSpinner = Mockito.mock();

            Mockito.doReturn(slotSpinner)
                .when(slotSpinnerFactory)
                .create(Mockito.any());

            Mockito.doReturn(List.of("BLACK", "BLACK", "BLACK", "WHITE"))
                .when(slotSpinner)
                .spin();

            mockMvc.perform(createRequest(machine.getId()))
                .andExpectAll(
                    MockMvcResultMatchers.status().is(200),
                    MockMvcResultMatchers.jsonPath("$.slots", Matchers.equalTo(List.of("BLACK", "BLACK", "BLACK", "WHITE"))),
                    MockMvcResultMatchers.jsonPath("$.outcome", Matchers.equalTo("SMALL_PRIZE")),
                    MockMvcResultMatchers.jsonPath("$.payoutInPence", Matchers.equalTo(500)),
                    MockMvcResultMatchers.jsonPath("$.remainingFloatInPence", Matchers.equalTo(9600)),
                    MockMvcResultMatchers.jsonPath("$.freeSpinsAwarded", Matchers.equalTo(0))
                );
        }

        @Test
        void returnsExpectedResult_whenLoss() throws Exception {
            final MachineEntity machine = createMachine();
            final SlotSpinner slotSpinner = Mockito.mock();

            Mockito.doReturn(slotSpinner)
                .when(slotSpinnerFactory)
                .create(Mockito.any());

            Mockito.doReturn(List.of("BLACK", "WHITE", "BLACK", "WHITE"))
                .when(slotSpinner)
                .spin();

            mockMvc.perform(createRequest(machine.getId()))
                .andExpectAll(
                    MockMvcResultMatchers.status().is(200),
                    MockMvcResultMatchers.jsonPath("$.slots", Matchers.equalTo(List.of("BLACK", "WHITE", "BLACK", "WHITE"))),
                    MockMvcResultMatchers.jsonPath("$.outcome", Matchers.equalTo("LOSS"))
                );
        }

        private RequestBuilder createRequest(Integer machineId) {
            return MockMvcRequestBuilders.post("/api/machines/{machineId}/spins", machineId)
                .header("x-version", "1")
                .header("x-user", "2")
                .contentType(MediaType.APPLICATION_JSON);
        }

        private MachineEntity createMachine() {
            final MachineEntity machine = MachineEntity.builder()
                .floatInPence(10000)
                .spinPriceInPence(100)
                .numberOfSlots(4)
                .colours(Set.of("BLACK", "WHITE", "GREEN", "YELLOW"))
                .smallPrizeAdjacency(2)
                .spinType(MachineEntity.SpinType.RANDOM)
                .build();

            machine.setNewEntity(true);

            return machineRepository.save(machine);
        }

    }

}
