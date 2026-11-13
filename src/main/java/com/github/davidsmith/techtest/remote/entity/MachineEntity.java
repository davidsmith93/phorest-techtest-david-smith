package com.github.davidsmith.techtest.remote.entity;

import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

import java.util.Set;

@Data
@Builder
@Table("FRUIT_MACHINE")
@EqualsAndHashCode(callSuper = true)
public class MachineEntity extends PersistableEntity<Integer> {
    @Id
    private Integer id;
    private Set<String> colours;
    private int numberOfSlots;
    private SpinType spinType;
    private int floatInPence;
    private int spinPriceInPence;
    private int smallPrizeAdjacency;

    public enum SpinType {
        RANDOM
    }
}
