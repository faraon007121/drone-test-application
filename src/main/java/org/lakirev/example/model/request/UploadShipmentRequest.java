package org.lakirev.example.model.request;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import org.lakirev.example.model.Coordinates;
import org.lakirev.example.model.ShipmentStatus;

import java.util.List;

public record UploadShipmentRequest(

        @NotNull
        Long droneId,

        @NotNull
        Coordinates destination,

        @NotNull
        ShipmentStatus status,

        String details,

        @NotNull
        @Size(min = 1)
        List<Long> medicationIds) {
}
