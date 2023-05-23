package org.lakirev.example.model.response;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.lakirev.example.model.Coordinates;
import org.lakirev.example.model.ShipmentStatus;

import java.time.Instant;
import java.util.List;

public record ShipmentInfo(
        Long id,
        @JsonIgnoreProperties("shipments")
        DroneFullInfo drone,
        ShipmentStatus status,
        String details,
        Instant createdDate,
        Instant updatedDate,
        Coordinates destination,
        @JsonIgnoreProperties("shipments")
        List<MedicationFullInfo> medications) {
}
