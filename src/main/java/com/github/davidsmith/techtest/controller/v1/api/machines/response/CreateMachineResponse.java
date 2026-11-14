package com.github.davidsmith.techtest.controller.v1.api.machines.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
public class CreateMachineResponse {
    private Integer id;
}
