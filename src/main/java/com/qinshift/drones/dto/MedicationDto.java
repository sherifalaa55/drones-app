package com.qinshift.drones.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class MedicationDto {

    @NotBlank(message = "Medication name is mandatory")
    @Size(min = 1, max = 100)
    @Pattern(regexp = "^[a-zA-Z0-9_-]+$", message = "Code must contain only letters, numbers, hyphens and underscores")
    private String name;

    @NotBlank(message = "Image is mandatory")
    private String image;

    @Pattern(regexp = "^[A-Z0-9_]+$", message = "Code must contain only uppercase letters, numbers and underscores")
    private String code;

    @Min(value = 1, message = "Weight must be greater than 0")
    private Double weight;
}
