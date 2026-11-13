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
            Assertions.assertThat(userRepository.findById(1))
                .isPresent()
                .get()
                .returns(1, UserEntity::getId);
        }
    }

}