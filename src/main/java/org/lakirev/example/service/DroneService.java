package org.lakirev.example.service;

import org.lakirev.example.model.response.DroneFullInfo;
import org.lakirev.example.model.response.DroneShortInfo;
import org.lakirev.example.model.request.UploadDroneRequest;

public interface DroneService {

    DroneShortInfo getById(Long id);

    DroneFullInfo getFullById(Long id);

    DroneShortInfo register(UploadDroneRequest request);

    DroneShortInfo update(Long id, UploadDroneRequest request);

    void delete(Long id);

}
