package com.github.davidsmith.techtest.remote.repository;

import com.github.davidsmith.techtest.remote.entity.UserEntity;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
class UserRepositoryTest {

    @Autowired
    UserRepository userRepository;

    @Nested
    class Get {
        @Test
        @Transactional
        void returnsExpected() {
            final UserEntity user = createUser();

            Assertions.assertThat(userRepository.findById(user.getId()))
                .isPresent()
                .get()
                .returns(user.getId(), UserEntity::getId);
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