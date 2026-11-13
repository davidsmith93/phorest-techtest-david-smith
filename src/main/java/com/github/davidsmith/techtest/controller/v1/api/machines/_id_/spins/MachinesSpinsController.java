package com.github.davidsmith.techtest.controller.v1.api.machines._id_.spins;

import com.github.davidsmith.techtest.controller.v1.api.machines._id_.spins.response.MachineSpinsResult;
import com.github.davidsmith.techtest.service.spins.SpinService;
import lombok.RequiredArgsConstructor;
import org.slf4j.MDC;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/api/machines/{machineId}/spins", headers = {"x-version=1"})
public class MachinesSpinsController {

    private final SpinService spinService;

    @PostMapping
    public ResponseEntity<MachineSpinsResult> create(
        @PathVariable Integer machineId,
        @RequestHeader("x-user") Integer userId
    ) {
        MDC.put("machine", "%d".formatted(machineId));
        MDC.put("user", "%d".formatted(userId));

        return ResponseEntity.ok(spinService.create(machineId, userId));
    }

}
