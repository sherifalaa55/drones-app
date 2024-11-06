package com.qinshift.drones.controllers;

import com.qinshift.drones.domain.Drone;
import com.qinshift.drones.domain.Medication;
import com.qinshift.drones.dto.DroneDto;
import com.qinshift.drones.dto.MedicationDto;
import com.qinshift.drones.enums.DroneState;
import com.qinshift.drones.repositories.DroneRepository;
import com.qinshift.drones.services.DroneService;
import jakarta.validation.Valid;
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

    @GetMapping("/{serialNumber}/battery")
    public ResponseEntity checkBatteryLevel(@PathVariable("serialNumber") String serialNumber) throws Exception {
        // get the drone by serial number
        Drone drone = droneRepo.findBySerialNumber(serialNumber)
                .orElseThrow(() -> new Exception(
                        "Drone with serial number " + serialNumber + " not found."));

        // fetch battery level
        return ResponseEntity.ok(drone.getBatteryCapacity());
    }

    @GetMapping("/{serialNumber}/medications")
    public ResponseEntity getLoadedMedications(@PathVariable("serialNumber") String serialNumber) throws Exception {
        // get drone by serial nubmer
        Drone drone = droneRepo.findBySerialNumber(serialNumber)
                .orElseThrow(() -> new Exception(
                        "Drone with serial number " + serialNumber + " not found."));

        List<Medication> medications = drone.getMedications();

        return ResponseEntity.ok(medications);
    }

    @PostMapping("/register")
    public ResponseEntity<Drone> resgisterDrone(@RequestBody @Validated DroneDto droneDto) throws Exception {
        Drone drone = droneService.register(droneDto);
        return new ResponseEntity<Drone>(drone, HttpStatus.CREATED);
    }

    @PostMapping("/{serialNumber}/load")
    public ResponseEntity loadDrone(@PathVariable("serialNumber") String serialNumber, @RequestBody List<@Valid MedicationDto> medDto) throws Exception {
        Drone drone = droneRepo.findBySerialNumber(serialNumber)
                .orElseThrow(() -> new Exception(
                        "Drone with serial number " + serialNumber + " not found."));
        drone = droneService.loadDrone(drone, medDto);
        return new ResponseEntity<Drone>(drone, HttpStatus.OK);
    }
}
