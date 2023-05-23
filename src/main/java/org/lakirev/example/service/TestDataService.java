package org.lakirev.example.service;

import jakarta.annotation.PostConstruct;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.experimental.FieldDefaults;
import org.lakirev.example.model.DroneModel;
import org.lakirev.example.model.ShipmentStatus;
import org.lakirev.example.model.entity.Drone;
import org.lakirev.example.model.entity.Medication;
import org.lakirev.example.model.entity.Shipment;
import org.lakirev.example.repository.DroneRepository;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Service;

import java.nio.file.Files;
import java.util.Arrays;
import java.util.Collections;
import java.util.UUID;

@ConditionalOnProperty("demo.enabled")
@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class TestDataService {

    DroneRepository repository;

    ResourceLoader resourceLoader;

    DroneManager droneManager;

    @PostConstruct
    public void init() {
        Drone drone1 = Drone.builder()
                .model(DroneModel.MIDDLEWEIGHT)
                .weightLimit(260)
                .serialNumber("582e17ea-825a-4ed6-8da4-465a692cc255")
                .shipments(Arrays.asList(
                        Shipment.builder()
                                .status(ShipmentStatus.DELIVERED)
                                .details("Medications were successfully delivered!")
                                .destinationLatitude(48.59867)
                                .destinationLongitude(37.99804)
                                .medications(Arrays.asList(
                                        Medication.builder()
                                                .code("AAB_31")
                                                .name("Vicasol 15mg x 20")
                                                .weight(8)
                                                .build(),
                                        Medication.builder()
                                                .code("AAC_17")
                                                .name("Bandage 16cm x 10m")
                                                .weight(50)
                                                .build(),
                                        Medication.builder()
                                                .code("BBW_16")
                                                .name("Splint")
                                                .weight(75)
                                                .build(),
                                        Medication.builder()
                                                .code("CPA_33")
                                                .name("Esmarh tourniquet")
                                                .weight(113)
                                                .build()
                                ))
                                .build(),
                        Shipment.builder()
                                .status(ShipmentStatus.FAILED)
                                .details("Drone has lost stuff while delivering")
                                .destinationLatitude(50.59826)
                                .destinationLongitude(36.59535)
                                .medications(Collections.singletonList(
                                        Medication.builder()
                                                .code("QEE_68")
                                                .name("First aid kit")
                                                .weight(250)
                                                .build()
                                ))
                                .build()))
                .build();
        for (Shipment shipment : drone1.getShipments()) {
            shipment.setDrone(drone1);
            for (Medication medication : shipment.getMedications()) {
                medication.setImage(getImage(medication.getName()));
                medication.setShipments(drone1.getShipments());
            }
        }
        Drone drone2 = Drone.builder()
                .model(DroneModel.HEAVYWEIGHT)
                .weightLimit(480)
                .serialNumber(UUID.randomUUID().toString())
                .build();
        Drone drone3 = Drone.builder()
                .model(DroneModel.CRUISERWEIGHT)
                .weightLimit(330)
                .serialNumber(UUID.randomUUID().toString())
                .build();
        Drone drone4 = Drone.builder()
                .model(DroneModel.LIGHTWEIGHT)
                .weightLimit(190)
                .serialNumber(UUID.randomUUID().toString())
                .build();
        if (!repository.existsBySerialNumber(drone1.getSerialNumber())) {
            repository.save(drone1);
            repository.save(drone2);
            repository.save(drone3);
            repository.save(drone4);
        }
        droneManager.connect(1L);
        droneManager.connect(2L);
        droneManager.connect(3L);
        droneManager.connect(4L);
    }

    @SneakyThrows
    private byte[] getImage(String name) {
        Resource resource = resourceLoader.getResource("classpath:/images/" + name + ".jpg");
        return resource.getInputStream().readAllBytes();
    }

}
