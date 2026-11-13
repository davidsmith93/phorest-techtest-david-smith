package com.github.davidsmith.techtest.remote.entity;

import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

@Data
@Builder
@Table("user_spins")
@EqualsAndHashCode(callSuper = true)
public class UserSpinsEntity extends PersistableEntity<UserSpinsEntity.Identifier> {
    @Id
    private Identifier id;
    private int freeSpins;

    @Data
    @Builder
    public static class Identifier {
        private Integer userId;
        private Integer fruitMachineId;
    }
}
