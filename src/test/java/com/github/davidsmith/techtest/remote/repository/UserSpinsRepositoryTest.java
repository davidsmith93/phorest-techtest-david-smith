package com.github.davidsmith.techtest.remote.repository;

import com.github.davidsmith.techtest.remote.entity.UserSpinsEntity;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
class UserSpinsRepositoryTest {

    @Autowired
    UserSpinsRepository userSpinsRepository;

    @Nested
    class Get {
        @Test
        @Transactional
        void returnsExpected() {
            final UserSpinsEntity.Identifier spinIdentifier = UserSpinsEntity.Identifier.builder()
                .userId(1)
                .fruitMachineId(1)
                .build();

            final UserSpinsEntity userSpins = UserSpinsEntity.builder()
                .id(UserSpinsEntity.Identifier.builder()
                    .userId(1)
                    .fruitMachineId(1)
                    .build())
                .freeSpins(0)
                .build();

            userSpins.setNewEntity(true);

            userSpinsRepository.save(userSpins);

            Assertions.assertThat(userSpinsRepository.findById(spinIdentifier))
                .isPresent()
                .get()
                .returns(spinIdentifier, UserSpinsEntity::getId)
                .returns(0, UserSpinsEntity::getFreeSpins);
        }
    }

}