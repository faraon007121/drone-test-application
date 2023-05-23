package org.lakirev.example.model.request;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import org.lakirev.example.model.DroneModel;


public record UploadDroneRequest(

        @NotNull
        @Size(max = 100, message = "Number of characters in serial number cannot exceed 100")
        String serialNumber,

        @NotNull
        DroneModel model,

        @NotNull
        @Positive(message = "Weight limit must be positive")
        @Max(value = 500, message = "Weight limit must be less than 500")
        Integer weightLimit) {

}