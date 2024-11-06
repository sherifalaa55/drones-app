package com.qinshift.drones.dto;

import com.qinshift.drones.enums.DroneState;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Getter
@Setter
@NoArgsConstructor
public class DroneDto {

    @Size(min = 1, max = 100)
    private String serialNumber;

    @Enumerated(EnumType.STRING)
    private String model;

    @Min(0)
    @Max(value = 100, message = "Battery Capacity must be less than or equal 100%")
    private Double batteryCapacity;

    @Min(1)
    private Double weightLimit;

    @Enumerated(EnumType.STRING)
    private DroneState state;
}
