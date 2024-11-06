package com.qinshift.drones.repositories;

import com.qinshift.drones.domain.Drone;
import com.qinshift.drones.enums.DroneState;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface DroneRepository extends JpaRepository<Drone, String> {

    Optional<Drone> findBySerialNumber(String serialNumber);

    List<Drone> findAllByState(DroneState state);
}
