package com.github.davidsmith.techtest.remote.repository;

import com.github.davidsmith.techtest.remote.entity.MachineEntity;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.util.Set;

class MachineRepositoryTest {

    @Nested
    class Get {
        @Test
        void returnsExpected() {
            Assertions.assertThat(new MachineRepository().get())
                .returns(4, MachineEntity::getNumberOfSlots)
                .returns(Set.of("BLACK", "WHITE", "GREEN", "YELLOW"), MachineEntity::getColours);
        }
    }

}