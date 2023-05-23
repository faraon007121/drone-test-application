package org.lakirev.example.data;

import org.lakirev.example.model.Coordinates;
import org.lakirev.example.model.ShipmentStatus;
import org.lakirev.example.model.entity.Shipment;
import org.lakirev.example.model.request.UploadShipmentRequest;
import org.lakirev.example.model.response.ShipmentInfo;

import java.util.Arrays;

public final class ShipmentTestData {

    public static UploadShipmentRequest getUploadRequest() {
        return new UploadShipmentRequest(
                1L,
                new Coordinates(50.45, 30.52),
                ShipmentStatus.IN_PROGRESS,
                "Nothing here",
                Arrays.asList(1L, 2L));
    }

    public static ShipmentInfo getShipmentInfo() {
        return new ShipmentInfo(
                1L,
                DroneTestData.getDroneFullInfo(),
                ShipmentStatus.IN_PROGRESS,
                "Nothing here",
                null,
                null,
                new Coordinates(50.45, 30.52),
                MedicationTestData.getMedicationFullInfoList());
    }

    public static Shipment getShipment() {
        return Shipment.builder()
                .drone(DroneTestData.getDrone())
                .destinationLatitude(50.45)
                .destinationLongitude(30.52)
                .status(ShipmentStatus.IN_PROGRESS)
                .details("Nothing here")
                .medications(MedicationTestData.getMedicationList())
                .build();
    }
}
