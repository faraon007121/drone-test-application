package org.lakirev.example.service;

import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.lakirev.example.data.DroneTestData;
import org.lakirev.example.data.MedicationTestData;
import org.lakirev.example.data.ShipmentTestData;
import org.lakirev.example.exception.NotFoundException;
import org.lakirev.example.mapper.ShipmentMapper;
import org.lakirev.example.mapper.ShipmentMapperImpl;
import org.lakirev.example.model.ShipmentStatus;
import org.lakirev.example.model.response.ShipmentInfo;
import org.lakirev.example.model.entity.Drone;
import org.lakirev.example.model.entity.Medication;
import org.lakirev.example.model.entity.Shipment;
import org.lakirev.example.model.request.UploadShipmentRequest;
import org.lakirev.example.repository.DroneRepository;
import org.lakirev.example.repository.MedicationRepository;
import org.lakirev.example.repository.ShipmentRepository;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;

@FieldDefaults(level = AccessLevel.PRIVATE)
@ExtendWith(MockitoExtension.class)
class DefaultShipmentServiceTest {

    @InjectMocks
    DefaultShipmentService service;

    @Spy
    ShipmentMapper mapper = new ShipmentMapperImpl();

    @Mock
    ShipmentRepository shipmentRepository;

    @Mock
    DroneRepository droneRepository;

    @Mock
    MedicationRepository medicationRepository;

    UploadShipmentRequest testRequest;

    ShipmentInfo testShipment;

    @BeforeEach
    void init() {
        testRequest = ShipmentTestData.getUploadRequest();
        testShipment = ShipmentTestData.getShipmentInfo();
    }

    @Test
    void testRegister() {
        Drone drone = DroneTestData.getDrone();
        List<Medication> medications = MedicationTestData.getMedicationList();
        Mockito.when(droneRepository.getReferenceById(testRequest.droneId())).thenReturn(drone);
        Mockito.when(medicationRepository.getReferenceById(testRequest.medicationIds().get(0))).thenReturn(medications.get(0));
        Mockito.when(medicationRepository.getReferenceById(testRequest.medicationIds().get(1))).thenReturn(medications.get(1));
        Mockito.when(shipmentRepository.save(any())).then(i -> {
            Object argument = i.getArgument(0);
            Shipment shipment = (Shipment) argument;
            shipment.setId(1L);
            return argument;
        });
        ShipmentInfo actual = service.register(testRequest);
        Assertions.assertEquals(testShipment, actual);
    }

    @Test
    void testUpdate() {
        UploadShipmentRequest request = new UploadShipmentRequest(
                1L,
                testRequest.destination(),
                ShipmentStatus.DELIVERED,
                "New details",
                testRequest.medicationIds());

        ShipmentInfo expected = new ShipmentInfo(
                1L,
                testShipment.drone(),
                ShipmentStatus.DELIVERED,
                "New details",
                null,
                null,
                testShipment.destination(),
                MedicationTestData.getMedicationFullInfoList());

        List<Medication> medications = MedicationTestData.getMedicationList();

        Mockito.when(shipmentRepository.findById(1L)).thenReturn(Optional.of(ShipmentTestData.getShipment()));
        Mockito.when(droneRepository.getReferenceById(1L)).thenReturn(DroneTestData.getDrone());
        Mockito.when(medicationRepository.getReferenceById(1L)).thenReturn(medications.get(0));
        Mockito.when(medicationRepository.getReferenceById(2L)).thenReturn(medications.get(1));
        Mockito.when(shipmentRepository.save(any())).then(args -> {
            Object argument = args.getArgument(0);
            Shipment shipment = (Shipment) argument;
            shipment.setId(1L);
            return argument;
        });

        Assertions.assertEquals(expected, service.update(1L, request));

    }

    @Test
    void testNotFoundThrowing() {
        Mockito.when(shipmentRepository.findById(1L)).thenReturn(Optional.empty());
        Mockito.when(shipmentRepository.findByIdFetchRelations(1L)).thenReturn(Optional.empty());
        Assertions.assertThrows(NotFoundException.class, () -> service.getById(1L));
        Assertions.assertThrows(NotFoundException.class, () -> service.update(1L, testRequest));
        Assertions.assertThrows(NotFoundException.class, () -> service.delete(1L));
    }

}