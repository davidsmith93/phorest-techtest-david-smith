package com.github.davidsmith.techtest.controller.v1.api.machines.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.util.Set;

@Data
@Builder
@AllArgsConstructor
public class RetrieveMachineResponse {
    private Integer id;
    private Set<String> colours;
    private int numberOfSlots;
    private int floatInPence;
    private int spinPriceInPence;
    private int smallPrizeAdjacency;
}
