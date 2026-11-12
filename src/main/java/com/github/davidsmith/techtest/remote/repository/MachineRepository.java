package com.github.davidsmith.techtest.remote.repository;

import com.github.davidsmith.techtest.remote.entity.MachineEntity;
import org.springframework.stereotype.Component;

import java.util.Set;

@Component
public class MachineRepository {

    private static final String BLACK = "BLACK";
    private static final String WHITE = "WHITE";
    private static final String GREEN = "GREEN";
    private static final String YELLOW = "YELLOW";

    private static final Set<String> COLOURS = Set.of(
        BLACK,
        WHITE,
        GREEN,
        YELLOW
    );

    private static final int NUMBER_OF_SLOTS = 4;

    public MachineEntity get() {
        return MachineEntity.builder()
            .colours(COLOURS)
            .numberOfSlots(NUMBER_OF_SLOTS)
            .spinType(MachineEntity.SpinType.RANDOM)
            .build();
    }

}
