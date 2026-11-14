package com.github.davidsmith.techtest.controller.v1.api.machines;

import com.github.davidsmith.techtest.controller.v1.api.machines.request.CreateMachineRequest;
import com.github.davidsmith.techtest.controller.v1.api.machines.request.UpdateMachineRequest;
import com.github.davidsmith.techtest.remote.entity.MachineEntity;
import com.github.davidsmith.techtest.remote.repository.MachineRepository;
import org.assertj.core.api.Assertions;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.transaction.annotation.Transactional;
import tools.jackson.databind.ObjectMapper;

import java.util.Set;

@SpringBootTest
@AutoConfigureMockMvc
class MachinesControllerIntegrationTest {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    @Autowired
    MachineRepository machineRepository;

    @Nested
    class Create {
        //Other Jakarta validation Omitted for brevity, but should be implemented for complete verification
        @Test
        void badRequest_whenColoursNull() throws Exception {
            CreateMachineRequest request = CreateMachineRequest.builder()
                .numberOfSlots(2)
                .floatInPence(1)
                .spinPriceInPence(1)
                .smallPrizeAdjacency(2)
                .build();

            mockMvc.perform(createRequest(request))
                .andExpectAll(
                    MockMvcResultMatchers.status().is(400),
                    MockMvcResultMatchers.jsonPath("$.code", Matchers.equalTo("error.validation")),
                    MockMvcResultMatchers.jsonPath("$.message", Matchers.equalTo("Validation failed for request")),
                    MockMvcResultMatchers.jsonPath("$.errors[0].code", Matchers.equalTo("colours")),
                    MockMvcResultMatchers.jsonPath("$.errors[0].error", Matchers.equalTo("must not be empty"))
                );
        }

        @Test
        void badRequest_whenUniqueColoursIncorrect() throws Exception {
            CreateMachineRequest request = CreateMachineRequest.builder()
                .colours(Set.of("BLACK", "black", "black "))
                .numberOfSlots(2)
                .floatInPence(1)
                .spinPriceInPence(1)
                .smallPrizeAdjacency(2)
                .build();

            mockMvc.perform(createRequest(request))
                .andExpectAll(
                    MockMvcResultMatchers.status().is(400),
                    MockMvcResultMatchers.jsonPath("$.code", Matchers.equalTo("error.bad_request")),
                    MockMvcResultMatchers.jsonPath("$.message", Matchers.equalTo("Invalid configuration")),
                    MockMvcResultMatchers.jsonPath("$.errors[0].code", Matchers.equalTo("bad_request")),
                    MockMvcResultMatchers.jsonPath("$.errors[0].error", Matchers.equalTo("Colours was fewer than minimum of 2"))
                );
        }

        @Test
        void badRequest_whenSmallPrizeAdjacencyMoreThanNumberOfSlots() throws Exception {
            CreateMachineRequest request = CreateMachineRequest.builder()
                .colours(Set.of("BLACK", "WHITE"))
                .numberOfSlots(2)
                .floatInPence(1)
                .spinPriceInPence(1)
                .smallPrizeAdjacency(3)
                .build();

            mockMvc.perform(createRequest(request))
                .andExpectAll(
                    MockMvcResultMatchers.status().is(400),
                    MockMvcResultMatchers.jsonPath("$.code", Matchers.equalTo("error.bad_request")),
                    MockMvcResultMatchers.jsonPath("$.message", Matchers.equalTo("Invalid configuration")),
                    MockMvcResultMatchers.jsonPath("$.errors[0].code", Matchers.equalTo("bad_request")),
                    MockMvcResultMatchers.jsonPath("$.errors[0].error", Matchers.equalTo("Small prize adjacency was greater than the number of slots"))
                );
        }

        @Test
        void createsNew_whenValid() throws Exception {
            CreateMachineRequest request = CreateMachineRequest.builder()
                .colours(Set.of("BLACK", "WHITE"))
                .numberOfSlots(2)
                .floatInPence(1)
                .spinPriceInPence(1)
                .smallPrizeAdjacency(2)
                .build();

            mockMvc.perform(createRequest(request))
                .andExpectAll(
                    MockMvcResultMatchers.status().is(201),
                    MockMvcResultMatchers.jsonPath("$.id", Matchers.notNullValue())
                );
        }

        private RequestBuilder createRequest(CreateMachineRequest request) {
            return MockMvcRequestBuilders.post("/api/machines")
                .header("x-version", "1")
                .content(objectMapper.writeValueAsBytes(request))
                .contentType(MediaType.APPLICATION_JSON);
        }
    }

    @Nested
    class Retrieve {
        @Test
        void notFound_whenMachineDoesNotExist() throws Exception {
            mockMvc.perform(createRequest(-1))
                .andExpectAll(
                    MockMvcResultMatchers.status().is(404),
                    MockMvcResultMatchers.jsonPath("$.code", Matchers.equalTo("error.not_found")),
                    MockMvcResultMatchers.jsonPath("$.message", Matchers.equalTo("Unable to find machine")),
                    MockMvcResultMatchers.jsonPath("$.errors[0].code", Matchers.equalTo("not_found")),
                    MockMvcResultMatchers.jsonPath("$.errors[0].error", Matchers.equalTo("Machine could not be found with id -1"))
                );
        }

        @Test
        @Transactional
        void returnsMachine_whenValid() throws Exception {
            MachineEntity machine = MachineEntity.builder()
                .colours(Set.of("BLACK", "WHITE"))
                .numberOfSlots(2)
                .floatInPence(1)
                .spinPriceInPence(1)
                .smallPrizeAdjacency(2)
                .spinType(MachineEntity.SpinType.RANDOM)
                .build();

            machine.setNewEntity(true);

            machine = machineRepository.save(machine);

            mockMvc.perform(createRequest(machine.getId()))
                .andExpectAll(
                    MockMvcResultMatchers.status().is(200),
                    MockMvcResultMatchers.jsonPath("$.id", Matchers.equalTo(machine.getId())),
                    MockMvcResultMatchers.jsonPath("$.colours", Matchers.containsInAnyOrder("BLACK", "WHITE")),
                    MockMvcResultMatchers.jsonPath("$.numberOfSlots", Matchers.equalTo(machine.getNumberOfSlots())),
                    MockMvcResultMatchers.jsonPath("$.floatInPence", Matchers.equalTo(machine.getFloatInPence())),
                    MockMvcResultMatchers.jsonPath("$.spinPriceInPence", Matchers.equalTo(machine.getSpinPriceInPence())),
                    MockMvcResultMatchers.jsonPath("$.smallPrizeAdjacency", Matchers.equalTo(machine.getSmallPrizeAdjacency()))
                );
        }

        private RequestBuilder createRequest(Integer machineId) {
            return MockMvcRequestBuilders.get("/api/machines/{machineId}", machineId)
                .header("x-version", "1")
                .contentType(MediaType.APPLICATION_JSON);
        }
    }

    @Nested
    class Update {
        //Other Jakarta validation Omitted for brevity, but should be implemented for complete verification
        @Test
        void badRequest_whenColoursNull() throws Exception {
            UpdateMachineRequest request = UpdateMachineRequest.builder()
                .numberOfSlots(2)
                .floatInPence(1)
                .spinPriceInPence(1)
                .smallPrizeAdjacency(2)
                .build();

            mockMvc.perform(createRequest(-1, request))
                .andExpectAll(
                    MockMvcResultMatchers.status().is(400),
                    MockMvcResultMatchers.jsonPath("$.code", Matchers.equalTo("error.validation")),
                    MockMvcResultMatchers.jsonPath("$.message", Matchers.equalTo("Validation failed for request")),
                    MockMvcResultMatchers.jsonPath("$.errors[0].code", Matchers.equalTo("colours")),
                    MockMvcResultMatchers.jsonPath("$.errors[0].error", Matchers.equalTo("must not be empty"))
                );
        }

        @Test
        void notFound_whenMachineNotFound() throws Exception {
            UpdateMachineRequest request = UpdateMachineRequest.builder()
                .colours(Set.of("BLACK", "WHITE"))
                .numberOfSlots(2)
                .floatInPence(1)
                .spinPriceInPence(1)
                .smallPrizeAdjacency(2)
                .build();

            mockMvc.perform(createRequest(-1, request))
                .andExpectAll(
                    MockMvcResultMatchers.status().is(404),
                    MockMvcResultMatchers.jsonPath("$.code", Matchers.equalTo("error.not_found")),
                    MockMvcResultMatchers.jsonPath("$.message", Matchers.equalTo("Unable to find machine")),
                    MockMvcResultMatchers.jsonPath("$.errors[0].code", Matchers.equalTo("not_found")),
                    MockMvcResultMatchers.jsonPath("$.errors[0].error", Matchers.equalTo("Machine could not be found with id -1"))
                );
        }

        @Test
        void badRequest_whenUniqueColoursIncorrect() throws Exception {
            final MachineEntity machineEntity = createMachineEntity();
            UpdateMachineRequest request = UpdateMachineRequest.builder()
                .colours(Set.of("BLACK", "black", "black "))
                .numberOfSlots(2)
                .floatInPence(1)
                .spinPriceInPence(1)
                .smallPrizeAdjacency(2)
                .build();

            mockMvc.perform(createRequest(machineEntity.getId(), request))
                .andExpectAll(
                    MockMvcResultMatchers.status().is(400),
                    MockMvcResultMatchers.jsonPath("$.code", Matchers.equalTo("error.bad_request")),
                    MockMvcResultMatchers.jsonPath("$.message", Matchers.equalTo("Invalid configuration")),
                    MockMvcResultMatchers.jsonPath("$.errors[0].code", Matchers.equalTo("bad_request")),
                    MockMvcResultMatchers.jsonPath("$.errors[0].error", Matchers.equalTo("Colours was fewer than minimum of 2"))
                );
        }

        @Test
        void badRequest_whenSmallPrizeAdjacencyMoreThanNumberOfSlots() throws Exception {
            final MachineEntity machineEntity = createMachineEntity();
            UpdateMachineRequest request = UpdateMachineRequest.builder()
                .colours(Set.of("BLACK", "WHITE"))
                .numberOfSlots(2)
                .floatInPence(1)
                .spinPriceInPence(1)
                .smallPrizeAdjacency(3)
                .build();

            mockMvc.perform(createRequest(machineEntity.getId(), request))
                .andExpectAll(
                    MockMvcResultMatchers.status().is(400),
                    MockMvcResultMatchers.jsonPath("$.code", Matchers.equalTo("error.bad_request")),
                    MockMvcResultMatchers.jsonPath("$.message", Matchers.equalTo("Invalid configuration")),
                    MockMvcResultMatchers.jsonPath("$.errors[0].code", Matchers.equalTo("bad_request")),
                    MockMvcResultMatchers.jsonPath("$.errors[0].error", Matchers.equalTo("Small prize adjacency was greater than the number of slots"))
                );
        }

        @Test
        void updatesExisting_whenValid() throws Exception {
            final MachineEntity machineEntity = createMachineEntity();
            UpdateMachineRequest request = UpdateMachineRequest.builder()
                .colours(Set.of("BLACK", "WHITE", "GREEN"))
                .numberOfSlots(3)
                .floatInPence(2)
                .spinPriceInPence(2)
                .smallPrizeAdjacency(3)
                .build();

            mockMvc.perform(createRequest(machineEntity.getId(), request))
                .andExpect(MockMvcResultMatchers.status().is(204));

            Assertions.assertThat(machineRepository.findById(machineEntity.getId()))
                .isPresent()
                .get()
                .returns(machineEntity.getId(), MachineEntity::getId)
                .returns(request.getColours(), MachineEntity::getColours)
                .returns(request.getNumberOfSlots(), MachineEntity::getNumberOfSlots)
                .returns(request.getFloatInPence(), MachineEntity::getFloatInPence)
                .returns(request.getSpinPriceInPence(), MachineEntity::getSpinPriceInPence)
                .returns(request.getSmallPrizeAdjacency(), MachineEntity::getSmallPrizeAdjacency);
        }

        private RequestBuilder createRequest(Integer machineId, UpdateMachineRequest request) {
            return MockMvcRequestBuilders.put("/api/machines/{machineId}", machineId)
                .header("x-version", "1")
                .content(objectMapper.writeValueAsBytes(request))
                .contentType(MediaType.APPLICATION_JSON);
        }

        private MachineEntity createMachineEntity() {
            MachineEntity machine = MachineEntity.builder()
                .colours(Set.of("BLACK", "WHITE"))
                .numberOfSlots(2)
                .floatInPence(1)
                .spinPriceInPence(1)
                .smallPrizeAdjacency(2)
                .spinType(MachineEntity.SpinType.RANDOM)
                .build();

            machine.setNewEntity(true);

            return machineRepository.save(machine);
        }
    }



}