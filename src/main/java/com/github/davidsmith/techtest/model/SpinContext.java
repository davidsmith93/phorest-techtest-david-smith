package com.github.davidsmith.techtest.model;

import com.github.davidsmith.techtest.remote.entity.MachineEntity;
import com.github.davidsmith.techtest.remote.entity.UserSpinsEntity;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@Builder
@RequiredArgsConstructor
public class SpinContext {
    private final SpinOutcome spinOutcome;
    private final MachineEntity machine;
    private final UserSpinsEntity userSpins;
}
