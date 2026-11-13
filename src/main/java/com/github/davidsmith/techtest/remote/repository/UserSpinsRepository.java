package com.github.davidsmith.techtest.remote.repository;

import com.github.davidsmith.techtest.remote.entity.UserSpinsEntity;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserSpinsRepository extends CrudRepository<UserSpinsEntity, UserSpinsEntity.Identifier> {
}
