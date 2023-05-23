package org.lakirev.example.model;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Data;
import lombok.experimental.FieldDefaults;
import org.lakirev.example.exception.drone.DroneOverweightException;
import org.lakirev.example.model.response.DroneShortInfo;
import org.lakirev.example.model.response.MedicationShortInfo;

import java.util.ArrayList;
import java.util.List;

@Data
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class DroneState implements Cloneable {

    DroneShortInfo drone;

    int batteryLevel;

    DroneStatus status;

    Coordinates destination;

    int weight;

    List<MedicationShortInfo> medications;

    public void addMedications(List<MedicationShortInfo> medications) {
        if (this.medications == null) {
            this.medications = new ArrayList<>();
        }
        Integer weightSum = medications.stream()
                .map(MedicationShortInfo::weight)
                .reduce(Integer::sum)
                .orElseThrow(() -> new IllegalArgumentException("Empty medication list provided"));
        if (weightSum + weight > drone.weightLimit()) {
            throw new DroneOverweightException("Cannot load medications, maximum weight exceeded, droneState: "
                    + this + ", medicationsToLoad: " + medications);
        }
        this.medications.addAll(medications);
        weight += weightSum;
    }

    public void removeMedications() {
        medications = null;
        weight = 0;
    }

    @Override
    public DroneState clone() {
        try {
            DroneState cloned = (DroneState) super.clone();
            List<MedicationShortInfo> medications = null;
            if (this.medications != null) {
                medications = new ArrayList<>(this.medications);
            }
            cloned.setDrone(new DroneShortInfo(drone.id(), drone.serialNumber(), drone.model(), drone.weightLimit()));
            cloned.setBatteryLevel(batteryLevel);
            cloned.setStatus(status);
            cloned.setDestination(destination);
            cloned.setWeight(weight);
            cloned.setMedications(medications);
            return cloned;
        } catch (CloneNotSupportedException e) {
            throw new RuntimeException(e);
        }
    }
}
