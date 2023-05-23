package org.lakirev.example.model.response;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.time.Instant;
import java.util.List;

public record MedicationFullInfo(
        Long id,
        String name,
        Integer weight,
        String code,
        Instant createdDate,
        Instant updatedDate,
        @JsonIgnoreProperties("medications")
        List<ShipmentInfo> shipments) {
}
