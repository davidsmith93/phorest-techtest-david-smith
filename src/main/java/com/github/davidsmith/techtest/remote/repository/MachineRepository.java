package com.github.davidsmith.techtest.remote.repository;

import com.github.davidsmith.techtest.remote.entity.MachineEntity;
import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface MachineRepository extends CrudRepository<MachineEntity, Integer> {

    @Query("SELECT * FROM fruit_machines WHERE id = :id FOR UPDATE")
    Optional<MachineEntity> findByIdForUpdate(Integer id);

}
