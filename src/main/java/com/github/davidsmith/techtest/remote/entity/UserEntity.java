package com.github.davidsmith.techtest.remote.entity;

import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

@Data
@Builder
@Table("users")
@EqualsAndHashCode(callSuper = true)
public class UserEntity extends PersistableEntity<Integer> {
    @Id
    private Integer id;
}
