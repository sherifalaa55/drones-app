package com.qinshift.drones.services;

import com.qinshift.drones.domain.Drone;
import com.qinshift.drones.domain.Medication;
import com.qinshift.drones.dto.DroneDto;
import com.qinshift.drones.dto.MedicationDto;
import com.qinshift.drones.enums.DroneState;
import com.qinshift.drones.repositories.DroneRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;


@Service
@RequiredArgsConstructor
public class DroneService {

    private static final Logger logger = LoggerFactory.getLogger(DroneService.class);

    private final ModelMapper modelMapper;

    private final DroneRepository droneRepo;

    public Drone register(DroneDto droneDto) throws Exception {
        long currentDroneCount = droneRepo.count();

        if (currentDroneCount == 10) {
            throw new Exception("Unable to register more than 10 drones");
        }

        Drone drone = modelMapper.map(droneDto, Drone.class);

        return droneRepo.saveAndFlush(drone);
    }

    public Drone loadDrone(Drone drone, List<MedicationDto> medsDto) throws Exception {

        // check if drone is idle
        if (!drone.getState().equals(DroneState.IDLE)) {
            throw new Exception("Drone must be idle");
        }

        if (drone.getBatteryCapacity() < 25) {
            throw new Exception("Cannot load drone with battery level below 25%");
        }

        double existingWeight = drone.getMedications().stream()
                .mapToDouble(Medication::getWeight)
                .sum();

        double newMedicationsWeight = medsDto.stream()
                .mapToDouble(MedicationDto::getWeight)
                .sum();

        if (drone.getWeightLimit() < existingWeight + newMedicationsWeight) {
            // throw exception
            throw new Exception("Medications weight exceed drone capacity");
        }

        List<Medication> medicationsToLoad = medsDto.stream()
                .map(medDto -> {
                    Medication med = modelMapper.map(medDto, Medication.class);
                    med.setDrone(drone);
                    return med;
                })
                .collect(Collectors.toList());

        drone.getMedications().addAll(medicationsToLoad);

        return droneRepo.saveAndFlush(drone);
    }

}
