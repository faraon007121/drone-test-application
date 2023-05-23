package org.lakirev.example.model.response;

import org.lakirev.example.model.DroneModel;

public record DroneShortInfo(
        Long id,
        String serialNumber,
        DroneModel model,
        Integer weightLimit) {
}
