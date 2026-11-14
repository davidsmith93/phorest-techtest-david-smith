package com.github.davidsmith.techtest.model;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class SpinOutcomeResult {
    private Integer payoutInPence;
    private Integer remainingFloatInPence;
    private Integer freeSpinsAwarded;
}
