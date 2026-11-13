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
            Assertions.assertThat(machineRepository.findById(1))
                .isPresent()
                .get()
                .returns(4, MachineEntity::getNumberOfSlots)
                .returns(Set.of("BLACK", "WHITE", "GREEN", "YELLOW"), MachineEntity::getColours);
        }
    }

}