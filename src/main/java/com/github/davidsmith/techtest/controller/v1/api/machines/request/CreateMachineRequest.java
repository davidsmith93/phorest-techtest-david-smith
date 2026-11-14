package com.github.davidsmith.techtest.controller.v1.api.machines.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.util.Set;

@Data
@Builder
@AllArgsConstructor
public class CreateMachineRequest {
    @NotEmpty
    @Size(min = 2)
    private Set<String> colours;

    @Min(2)
    private int numberOfSlots;

    @Min(1)
    private int floatInPence;

    @Min(1)
    private int spinPriceInPence;

    @Min(2)
    private int smallPrizeAdjacency;
}
