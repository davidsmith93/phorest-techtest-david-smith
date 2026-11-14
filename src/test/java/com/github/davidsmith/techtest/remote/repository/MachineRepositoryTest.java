package com.github.davidsmith.techtest.remote.repository;

import com.github.davidsmith.techtest.remote.entity.MachineEntity;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.Set;

@SpringBootTest
class MachineRepositoryTest {

    @Autowired
    MachineRepository machineRepository;

    @Nested
    class Get {
        @Test
        @Transactional
        void returnsExpected() {
            MachineEntity machine = createMachine();

            Assertions.assertThat(machineRepository.findById(machine.getId()))
                .isPresent()
                .get()
                .returns(4, MachineEntity::getNumberOfSlots)
                .returns(Set.of("BLACK", "WHITE", "GREEN", "YELLOW"), MachineEntity::getColours);
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