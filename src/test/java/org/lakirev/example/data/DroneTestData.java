package org.lakirev.example.data;

import org.lakirev.example.model.DroneModel;
import org.lakirev.example.model.entity.Drone;
import org.lakirev.example.model.request.UploadDroneRequest;
import org.lakirev.example.model.response.DroneFullInfo;
import org.lakirev.example.model.response.DroneShortInfo;

public final class DroneTestData {

    public static UploadDroneRequest getUploadRequest() {
        return new UploadDroneRequest(
                "testSerialNumber",
                DroneModel.LIGHTWEIGHT,
                150);
    }

    public static DroneShortInfo getDroneShortInfo() {
        return new DroneShortInfo(
                1L,
                "testSerialNumber",
                DroneModel.LIGHTWEIGHT,
                150);
    }

    public static DroneFullInfo getDroneFullInfo() {
        return new DroneFullInfo(
                1L,
                "testSerialNumber",
                DroneModel.LIGHTWEIGHT,
                150,
                null,
                null,
                null);
    }

    public static Drone getDrone() {
        return Drone.builder()
                .id(1L)
                .serialNumber("testSerialNumber")
                .model(DroneModel.LIGHTWEIGHT)
                .weightLimit(150)
                .build();
    }
}
