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
import org.lakirev.example.exception.drone.DroneAlreadyConnectedException;
import org.lakirev.example.exception.drone.DroneNotConnectedException;
import org.lakirev.example.exception.drone.DroneOverweightException;
import org.lakirev.example.model.Coordinates;
import org.lakirev.example.model.DroneState;
import org.lakirev.example.model.DroneStatus;
import org.lakirev.example.model.response.MedicationShortInfo;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;

@FieldDefaults(level = AccessLevel.PRIVATE)
@ExtendWith(MockitoExtension.class)
class DemoDroneManagerTest {

    @InjectMocks
    DemoDroneManager manager;

    @Mock
    DroneService droneService;

    @Mock
    MedicationService medicationService;

    @Mock
    ShipmentService shipmentService;

    @BeforeEach
    void init() {
        Mockito.when(droneService.getById(1L)).thenReturn(DroneTestData.getDroneShortInfo());
    }

    @Test
    void testCreation() {
        manager.connect(1L);
        Assertions.assertNotNull(manager.getDroneState(1L));
        Assertions.assertThrows(DroneAlreadyConnectedException.class, () -> manager.connect(1L));
    }

    @Test
    void testCheckConnection() {
        Assertions.assertThrows(DroneNotConnectedException.class, () -> manager.getDroneState(1L));
        Assertions.assertThrows(DroneNotConnectedException.class, () -> manager.loadDroneWithMedications(1L, null));
        Assertions.assertThrows(DroneNotConnectedException.class, () -> manager.setDestination(1L, null));
        Assertions.assertThrows(DroneNotConnectedException.class, () -> manager.launchDrone(1L));
        manager.connect(1L);
        Assertions.assertDoesNotThrow(() -> manager.getDroneState(1L));
    }

    @Test
    void testActionSequence() {
        manager.connect(1L);
        DroneState state = manager.getDroneState(1L);

        Assertions.assertEquals(DroneStatus.IDLE, state.getStatus());

        List<Long> medicationIds = Arrays.asList(1L, 2L);

        Mockito.when(medicationService.getByIdIn(medicationIds)).thenReturn(MedicationTestData.getMedicationShortInfoList());

        manager.loadDroneWithMedications(1L, medicationIds);
        state = manager.getDroneState(1L);

        Assertions.assertEquals(DroneStatus.LOADING, state.getStatus());
        Assertions.assertEquals(MedicationTestData.getMedicationShortInfoList(), state.getMedications());

        Coordinates destination = new Coordinates(50.35, 30.45);

        manager.setDestination(1L, destination);
        state = manager.getDroneState(1L);

        Assertions.assertEquals(DroneStatus.LOADED, state.getStatus());
        Assertions.assertEquals(destination, state.getDestination());

        Mockito.when(shipmentService.register(any())).thenReturn(ShipmentTestData.getShipmentInfo());

        manager.launchDrone(1L);
        state = manager.getDroneState(1L);

        Assertions.assertEquals(DroneStatus.DELIVERING, state.getStatus());

    }

    @Test
    void testOverweight() {
        List<Long> medicationIds = Arrays.asList(1L, 2L);
        List<MedicationShortInfo> medications = MedicationTestData.getMedicationShortInfoList();
        Integer portionWeight = medications.stream()
                .map(MedicationShortInfo::weight)
                .reduce(Integer::sum)
                .orElseThrow();
        Integer weightLimit = DroneTestData.getDroneShortInfo().weightLimit();

        manager.connect(1L);

        Mockito.when(medicationService.getByIdIn(medicationIds)).thenReturn(medications);

        for (int i = 0; i < weightLimit / portionWeight; i++) {
            manager.loadDroneWithMedications(1L, medicationIds);
        }

        Assertions.assertThrows(DroneOverweightException.class, () -> manager.loadDroneWithMedications(1L, medicationIds));
    }

    @Test
    void testLoadUnload() {
        List<Long> medicationIds = Arrays.asList(1L, 2L);
        List<MedicationShortInfo> medications = MedicationTestData.getMedicationShortInfoList();
        Integer expectedWeight = medications.stream()
                .map(MedicationShortInfo::weight)
                .reduce(Integer::sum)
                .orElseThrow();

        manager.connect(1L);

        Mockito.when(medicationService.getByIdIn(medicationIds)).thenReturn(medications);

        manager.loadDroneWithMedications(1L, medicationIds);

        DroneState state = manager.getDroneState(1L);
        Assertions.assertEquals(expectedWeight, state.getWeight());
        Assertions.assertNotNull(state.getMedications());

        manager.unloadMedications(1L);

        state = manager.getDroneState(1L);

        Assertions.assertEquals(0L, state.getWeight());
        Assertions.assertNull(state.getMedications());
    }

    @Test
    void testDisconnect() {
        manager.connect(1L);

        Assertions.assertNotNull(manager.getDroneState(1L));

        manager.disconnect(1L);

        Assertions.assertThrows(DroneNotConnectedException.class, () -> manager.getDroneState(1L));
    }

}