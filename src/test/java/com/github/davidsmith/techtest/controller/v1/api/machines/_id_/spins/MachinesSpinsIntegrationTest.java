package com.github.davidsmith.techtest.controller.v1.api.machines._id_.spins;

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

@SpringBootTest
@AutoConfigureMockMvc
public class MachinesSpinsIntegrationTest {

    @Autowired
    MockMvc mockMvc;

    @MockitoBean
    SlotSpinnerFactory slotSpinnerFactory;

    @Nested
    class Create {

        @Test
        void returnsExpectedResult_whenJackpot() throws Exception {
            final SlotSpinner slotSpinner = Mockito.mock();

            Mockito.doReturn(slotSpinner)
                .when(slotSpinnerFactory)
                .create(Mockito.any());

            Mockito.doReturn(List.of("BLACK", "BLACK", "BLACK", "BLACK"))
                .when(slotSpinner)
                .spin();

            mockMvc.perform(createRequest())
                .andExpectAll(
                    MockMvcResultMatchers.status().is(200),
                    MockMvcResultMatchers.jsonPath("$.slots", Matchers.equalTo(List.of("BLACK", "BLACK", "BLACK", "BLACK"))),
                    MockMvcResultMatchers.jsonPath("$.outcome", Matchers.equalTo("JACKPOT"))
                );
        }

        @Test
        void returnsExpectedResult_whenLoss() throws Exception {
            final SlotSpinner slotSpinner = Mockito.mock();

            Mockito.doReturn(slotSpinner)
                .when(slotSpinnerFactory)
                .create(Mockito.any());

            Mockito.doReturn(List.of("BLACK", "BLACK", "BLACK", "WHITE"))
                .when(slotSpinner)
                .spin();

            mockMvc.perform(createRequest())
                .andExpectAll(
                    MockMvcResultMatchers.status().is(200),
                    MockMvcResultMatchers.jsonPath("$.slots", Matchers.equalTo(List.of("BLACK", "BLACK", "BLACK", "WHITE"))),
                    MockMvcResultMatchers.jsonPath("$.outcome", Matchers.equalTo("LOSS"))
                );
        }

        RequestBuilder createRequest() {
            return MockMvcRequestBuilders.post("/api/machines/1/spins")
                .header("x-version", "1")
                .contentType(MediaType.APPLICATION_JSON);
        }

    }

}
