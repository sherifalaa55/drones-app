package com.qinshift.drones.domain;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.qinshift.drones.enums.DroneModel;
import com.qinshift.drones.enums.DroneState;
import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;

import java.util.*;

@Entity
@Setter
@Getter
@Table(name = "drones")
@RequiredArgsConstructor
public class Drone {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id", updatable = false, nullable = false)
    private String id;

    @Size(min =  1, max =  100)
    private String serialNumber;

    @Column(name = "weight_limit", nullable = false)
    private Double weightLimit;

    @Column(name = "battery_capacity", nullable = false)
    private Double batteryCapacity;

    @Enumerated(EnumType.STRING)
    private DroneState state;

    @Enumerated(EnumType.STRING)
    private DroneModel model;

    @OneToMany(mappedBy = "drone", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference
    private List<Medication> medications = new ArrayList<>();
}
