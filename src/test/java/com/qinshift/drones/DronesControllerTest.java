package com.qinshift.drones;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.qinshift.drones.controllers.DronesController;
import com.qinshift.drones.domain.Drone;
import com.qinshift.drones.domain.Medication;
import com.qinshift.drones.dto.DroneDto;
import com.qinshift.drones.dto.MedicationDto;
import com.qinshift.drones.enums.DroneModel;
import com.qinshift.drones.enums.DroneState;
import com.qinshift.drones.repositories.DroneRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.http.MediaType;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
public class DronesControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private DroneRepository droneRepository;

    private Drone existingDrone;

    @BeforeEach
    public void setup() {
        droneRepository.deleteAll();

        Drone drone = new Drone();
        drone.setSerialNumber("DRONE001");
        drone.setState(DroneState.IDLE);
        drone.setModel(DroneModel.CRUISERWEIGHT);
        drone.setBatteryCapacity(80.0);
        drone.setWeightLimit(500.0);
        droneRepository.save(drone);

        existingDrone = drone;

        Medication med1 = new Medication();
        med1.setName("Aspirin");
        med1.setCode("ASP_001");
        med1.setWeight(50.0);
        med1.setImage("http://example.com/images/aspirin.png");
        med1.setDrone(existingDrone);

        Medication med2 = new Medication();
        med2.setName("Paracetamol");
        med2.setCode("PARA_002");
        med2.setWeight(30.0);
        med2.setImage("http://example.com/images/paracetamol.png");
        med2.setDrone(existingDrone);

        List<Medication> medicationsToLoad = Arrays.asList(med1, med2);

        existingDrone.getMedications().addAll(medicationsToLoad);

        droneRepository.save(drone);
    }

    @Test
    public void registerDroneTest() throws Exception {
        DroneDto droneDto = new DroneDto();
        droneDto.setModel(DroneModel.LIGHTWEIGHT.toString());
        droneDto.setState(DroneState.IDLE);
        droneDto.setBatteryCapacity(80.0);
        droneDto.setWeightLimit(70.0);
        droneDto.setSerialNumber("drone-01");


        String jsonRequest = objectMapper.writeValueAsString(droneDto);

        mockMvc.perform(post("/api/v1/drones/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonRequest))
                .andExpect(status().isCreated())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON));
    }

    @Test
    public void loadDroneTest() throws Exception {
        MedicationDto med1 = new MedicationDto("Aspirin", "http://example.com/images/aspirin.png", "ASP_01", 5.5);
        MedicationDto med2 = new MedicationDto("Paracetamol", "http://example.com/images/paracetamol.png", "PAR_01", 10.0);

        mockMvc.perform(post("/api/v1/drones/{serialNumber}/load", existingDrone.getSerialNumber())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(Arrays.asList(med1, med2))))
                .andExpect(status().isOk());
    }

    @Test
    public void getLoadedMedicationsTest() throws Exception {
        mockMvc.perform(get("/api/v1/drones/{serialNumber}/medications", existingDrone.getSerialNumber())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].name").value("Aspirin"))
                .andExpect(jsonPath("$[0].code").value("ASP_001"))
                .andExpect(jsonPath("$[0].weight").value(50.0))
                .andExpect(jsonPath("$[0].image").value("http://example.com/images/aspirin.png"))
                .andExpect(jsonPath("$[1].name").value("Paracetamol"))
                .andExpect(jsonPath("$[1].code").value("PARA_002"))
                .andExpect(jsonPath("$[1].weight").value(30.0))
                .andExpect(jsonPath("$[1].image").value("http://example.com/images/paracetamol.png"));
    }

    @Test
    public void getAvailableDronesTest() throws Exception {
        mockMvc.perform(get("/api/v1/drones/available")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$.length()").value(1))
                .andExpect(jsonPath("$[0].serialNumber").value(existingDrone.getSerialNumber()))
                .andExpect(jsonPath("$[0].model").value(existingDrone.getModel().toString()));
    }

    @Test
    public void getDroneBatteryLevelTest() throws Exception {
        mockMvc.perform(get("/api/v1/drones/{serialNumber}/battery", existingDrone.getSerialNumber())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.batteryLevel").value(existingDrone.getBatteryCapacity()))
                .andExpect(jsonPath("$.serialNumber").value(existingDrone.getSerialNumber()));
    }
}
