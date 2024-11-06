package com.qinshift.drones.services;

import com.qinshift.drones.domain.Drone;
import com.qinshift.drones.dto.DroneDto;
import com.qinshift.drones.repositories.DroneRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;


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

}
