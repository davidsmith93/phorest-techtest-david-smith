package com.github.davidsmith.techtest.remote.repository;

import com.github.davidsmith.techtest.remote.entity.MachineEntity;
import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.jdbc.repository.support.SimpleJdbcRepository;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.Set;

@Repository
public interface MachineRepository extends CrudRepository<MachineEntity, Integer> {

    @Query("SELECT * FROM FRUIT_MACHINE WHERE id = :id FOR UPDATE")
    Optional<MachineEntity> findById(Integer id);

}
