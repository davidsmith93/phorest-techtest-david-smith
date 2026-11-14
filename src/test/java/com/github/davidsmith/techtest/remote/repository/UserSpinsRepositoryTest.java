package com.github.davidsmith.techtest.remote.repository;

import com.github.davidsmith.techtest.remote.entity.MachineEntity;
import com.github.davidsmith.techtest.remote.entity.UserEntity;
import com.github.davidsmith.techtest.remote.entity.UserSpinsEntity;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import org.testcontainers.shaded.org.checkerframework.checker.units.qual.A;

import java.util.Set;

@SpringBootTest
class UserSpinsRepositoryTest {

    @Autowired
    MachineRepository machineRepository;

    @Autowired
    UserRepository userRepository;

    @Autowired
    UserSpinsRepository userSpinsRepository;

    @Nested
    class Get {
        @Test
        @Transactional
        void returnsExpected() {
            MachineEntity machine = createMachine();
            UserEntity user = createUser();

            final UserSpinsEntity.Identifier spinIdentifier = UserSpinsEntity.Identifier.builder()
                .userId(user.getId())
                .fruitMachineId(machine.getId())
                .build();

            final UserSpinsEntity userSpins = UserSpinsEntity.builder()
                .id(UserSpinsEntity.Identifier.builder()
                    .userId(user.getId())
                    .fruitMachineId(machine.getId())
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

        private UserEntity createUser() {
            final UserEntity user = UserEntity.builder()
                .id(1)
                .build();

            user.setNewEntity(true);

            return userRepository.save(user);
        }
    }

}