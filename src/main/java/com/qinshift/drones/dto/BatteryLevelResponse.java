package com.qinshift.drones.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class BatteryLevelResponse {
    private String serialNumber;
    private double batteryLevel;
}
