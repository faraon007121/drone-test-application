package org.lakirev.example.service;

import org.lakirev.example.model.response.ShipmentInfo;
import org.lakirev.example.model.request.UploadShipmentRequest;

public interface ShipmentService {

    ShipmentInfo getById(Long id);

    ShipmentInfo register(UploadShipmentRequest request);

    ShipmentInfo update(Long id, UploadShipmentRequest request);

    void delete(Long id);

}
