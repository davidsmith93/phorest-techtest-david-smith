package com.github.davidsmith.techtest.controller.v1.api.machines._id_.spins;

import com.github.davidsmith.techtest.controller.v1.api.machines._id_.spins.response.MachineSpinsResult;
import com.github.davidsmith.techtest.service.spins.SpinService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/api/machines/{id}/spins", headers = {"x-version=1"})
public class MachinesSpinsController {

    private final SpinService spinService;

    @PostMapping
    public ResponseEntity<MachineSpinsResult> create() {
        return ResponseEntity.ok(spinService.create());
    }

}
