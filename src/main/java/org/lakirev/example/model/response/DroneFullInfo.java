package org.lakirev.example.model.response;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.lakirev.example.model.DroneModel;

import java.time.Instant;
import java.util.List;

public record DroneFullInfo(
        Long id,
        String serialNumber,
        DroneModel model,
        Integer weightLimit,
        Instant createdDate,
        Instant updatedDate,
        @JsonIgnoreProperties("drone")
        List<ShipmentInfo> shipments) {
}