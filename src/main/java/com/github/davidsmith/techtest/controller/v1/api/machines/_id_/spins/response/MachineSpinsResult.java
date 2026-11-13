package com.github.davidsmith.techtest.controller.v1.api.machines._id_.spins.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
public class MachineSpinsResult {
    private List<String> slots;
    private String outcome;
    private Integer payoutInPence;
    private Integer remainingFloatInPence;
    private Integer freeSpinsAwarded;
}
