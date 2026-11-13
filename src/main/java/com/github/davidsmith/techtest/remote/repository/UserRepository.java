package com.github.davidsmith.techtest.remote.repository;

import com.github.davidsmith.techtest.remote.entity.UserEntity;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends CrudRepository<UserEntity, Integer> {
}
