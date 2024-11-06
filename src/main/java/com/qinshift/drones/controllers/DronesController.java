package com.qinshift.drones.controllers;

import com.qinshift.drones.domain.Drone;
import com.qinshift.drones.dto.DroneDto;
import com.qinshift.drones.enums.DroneState;
import com.qinshift.drones.repositories.DroneRepository;
import com.qinshift.drones.services.DroneService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@Validated
@RequiredArgsConstructor
@RequestMapping("/api/v1/drones")
public class DronesController {

    private final DroneService droneService;
    @Autowired
    private final DroneRepository droneRepo;

    @GetMapping("/available")
    public ResponseEntity GetAvailableDrones() {
        List<Drone> drones = droneRepo.findAllByState(DroneState.IDLE);

        return ResponseEntity.ok(drones);
    }

    @PostMapping("/register")
    public ResponseEntity<Drone> resgisterDrone(@RequestBody @Validated DroneDto droneDto) throws Exception {
        Drone drone = droneService.register(droneDto);
        return new ResponseEntity<Drone>(drone, HttpStatus.CREATED);
    }
}
