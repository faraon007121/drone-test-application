package org.lakirev.example.service;

import org.lakirev.example.model.Coordinates;
import org.lakirev.example.model.DroneState;

import java.util.List;

public interface DroneManager {

    List<DroneState> getAvailableDrones();

    DroneState getDroneState(Long droneId);

    void connect(Long droneId);

    void disconnect(Long droneId);

    void loadDroneWithMedications(Long droneId, List<Long> medicationIdList);

    void unloadMedications(Long droneId);

    void setDestination(Long droneId, Coordinates destination);

    void launchDrone(Long droneId);

}
