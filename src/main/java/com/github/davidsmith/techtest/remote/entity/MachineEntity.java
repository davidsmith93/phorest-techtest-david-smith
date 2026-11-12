package com.github.davidsmith.techtest.remote.entity;

import lombok.Builder;
import lombok.Data;

import java.util.Set;

@Data
@Builder
public class MachineEntity {
    private Set<String> colours;
    private int numberOfSlots;
    private SpinType spinType;

    public enum SpinType {
        RANDOM
    }
}
