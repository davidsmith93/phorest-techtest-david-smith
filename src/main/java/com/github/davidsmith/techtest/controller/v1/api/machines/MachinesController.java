package com.github.davidsmith.techtest.controller.v1.api.machines;

import com.github.davidsmith.techtest.controller.v1.api.machines.request.CreateMachineRequest;
import com.github.davidsmith.techtest.controller.v1.api.machines.request.UpdateMachineRequest;
import com.github.davidsmith.techtest.controller.v1.api.machines.response.CreateMachineResponse;
import com.github.davidsmith.techtest.controller.v1.api.machines.response.RetrieveMachineResponse;
import com.github.davidsmith.techtest.service.machines.MachinesService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/api/machines", headers = {"x-version=1"})
public class MachinesController {

    private final MachinesService machinesService;

    @GetMapping("/{machineId}")
    public ResponseEntity<RetrieveMachineResponse> retrieve(@PathVariable Integer machineId) {
        return ResponseEntity.ok(machinesService.retrieve(machineId));
    }

    @PostMapping
    public ResponseEntity<CreateMachineResponse> create(@Valid @RequestBody CreateMachineRequest createRequest) {
        return ResponseEntity.status(HttpStatus.CREATED).body(machinesService.create(createRequest));
    }

    @PutMapping("/{machineId}")
    public ResponseEntity<Void> update(
        @Valid @RequestBody UpdateMachineRequest updateRequest,
        @PathVariable Integer machineId
    ) {
        machinesService.update(machineId, updateRequest);
        return ResponseEntity.noContent().build();
    }

}
