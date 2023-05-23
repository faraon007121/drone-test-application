package org.lakirev.example.service;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.lakirev.example.exception.drone.DestinationNotDefinedException;
import org.lakirev.example.exception.drone.DroneAlreadyConnectedException;
import org.lakirev.example.exception.drone.DroneNotConnectedException;
import org.lakirev.example.exception.drone.InsufficientDroneStateException;
import org.lakirev.example.exception.drone.LowBatteryLevelException;
import org.lakirev.example.model.Coordinates;
import org.lakirev.example.model.DroneState;
import org.lakirev.example.model.DroneStatus;
import org.lakirev.example.model.ShipmentStatus;
import org.lakirev.example.model.request.UploadShipmentRequest;
import org.lakirev.example.model.response.DroneShortInfo;
import org.lakirev.example.model.response.MedicationShortInfo;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class DemoDroneManager implements DroneManager {

    Map<Long, DroneState> droneMap = new HashMap<>();

    DroneService droneService;

    MedicationService medicationService;

    ShipmentService shipmentService;

    @Override
    public List<DroneState> getAvailableDrones() {
        return droneMap.values()
                .stream()
                .filter(e -> e.getStatus().equals(DroneStatus.IDLE))
                .map(DroneState::clone)
                .collect(Collectors.toList());
    }

    @Override
    public DroneState getDroneState(Long droneId) {
        checkConnected(droneId);
        return droneMap.get(droneId).clone();
    }

    @Override
    public void connect(Long droneId) {
        if (droneMap.containsKey(droneId)) {
            throw new DroneAlreadyConnectedException("Drone with id = " + droneId + " is already connected");
        }
        DroneShortInfo drone = droneService.getById(droneId);
        droneMap.put(droneId, DroneState.builder()
                .drone(drone)
                .batteryLevel(100)
                .status(DroneStatus.IDLE)
                .build());
    }

    @Override
    public void disconnect(Long droneId) {
        checkConnected(droneId);
        DroneState state = droneMap.get(droneId);
        DroneStatus status = state.getStatus();
        if (!status.equals(DroneStatus.IDLE)) {
            throw new InsufficientDroneStateException("Drone cannot be disconnected, droneId: " + droneId + ", status: " + status);
        }
        droneMap.remove(droneId);
    }

    @Override
    public void loadDroneWithMedications(Long droneId, List<Long> medicationIdList) {
        checkConnected(droneId);
        DroneState state = droneMap.get(droneId);
        checkBatteryLevel(droneId, state);
        DroneStatus status = state.getStatus();
        if (!status.equals(DroneStatus.LOADING) && !status.equals(DroneStatus.IDLE)) {
            throw new InsufficientDroneStateException("Cannot load medication to Drone, droneId: " + droneId + ", status: " + status);
        }
        List<MedicationShortInfo> medications = medicationService.getByIdIn(medicationIdList);
        state.addMedications(medications);
        if (status.equals(DroneStatus.IDLE)) {
            state.setStatus(DroneStatus.LOADING);
        }
    }

    @Override
    public void unloadMedications(Long droneId) {
        checkConnected(droneId);
        DroneState state = droneMap.get(droneId);
        checkBatteryLevel(droneId, state);
        DroneStatus status = state.getStatus();
        if (!status.equals(DroneStatus.LOADING) && !status.equals(DroneStatus.LOADED)) {
            throw new InsufficientDroneStateException("Cannot unload medications, droneId: " + droneId + ", status: " + status);
        }
        state.removeMedications();
        state.setDestination(null);
        state.setStatus(DroneStatus.IDLE);
    }

    @Override
    public void setDestination(Long droneId, Coordinates destination) {
        checkConnected(droneId);
        DroneState state = droneMap.get(droneId);
        checkBatteryLevel(droneId, state);
        if (!state.getStatus().equals(DroneStatus.LOADING)) {
            throw new InsufficientDroneStateException("To set delivery destination Drone must be in a Loading status, droneId: "
                    + droneId + ", droneStatus: " + state.getStatus());
        }
        state.setDestination(destination);
        state.setStatus(DroneStatus.LOADED);
    }

    @Override
    public void launchDrone(Long droneId) {
        checkConnected(droneId);
        DroneState state = droneMap.get(droneId);
        checkBatteryLevel(droneId, state);
        if (!state.getStatus().equals(DroneStatus.LOADED)) {
            throw new InsufficientDroneStateException("Cannot launch a Drone, droneId: " + droneId + ", status: " + state.getStatus());
        }
        if (state.getDestination() == null) {
            throw new DestinationNotDefinedException("No destination defined to launch a Drone with id = " + droneId);
        }
        state.setStatus(DroneStatus.DELIVERING);
        shipmentService.register(new UploadShipmentRequest(
                state.getDrone().id(),
                state.getDestination(),
                ShipmentStatus.IN_PROGRESS,
                "Drone was launched",
                state.getMedications().stream().map(MedicationShortInfo::id).collect(Collectors.toList())));
    }

    private void checkBatteryLevel(Long droneId, DroneState state) {
        if (state.getStatus().equals(DroneStatus.CHARGING)) {
            if (state.getBatteryLevel() < 80) {
                throw new LowBatteryLevelException("Drone is now charging and battery level is less than 80 percent, droneId: " + droneId);
            }
            state.setStatus(DroneStatus.IDLE);
        }
        if (state.getBatteryLevel() < 25) {
            state.setStatus(DroneStatus.CHARGING);
            throw new LowBatteryLevelException("Drone has a battery level less than 25 percent, droneId: " + droneId);
        }
    }

    private void checkConnected(Long droneId) {
        if (!droneMap.containsKey(droneId)) {
            throw new DroneNotConnectedException("No Drone with id = " + droneId + " is connected");
        }
    }

}
