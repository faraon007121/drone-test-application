package org.lakirev.example.service;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.lakirev.example.exception.NotFoundException;
import org.lakirev.example.mapper.ShipmentMapper;
import org.lakirev.example.model.entity.Shipment;
import org.lakirev.example.model.request.UploadShipmentRequest;
import org.lakirev.example.model.response.ShipmentInfo;
import org.lakirev.example.repository.DroneRepository;
import org.lakirev.example.repository.MedicationRepository;
import org.lakirev.example.repository.ShipmentRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@Slf4j
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class DefaultShipmentService implements ShipmentService {

    ShipmentRepository shipmentRepository;

    DroneRepository droneRepository;

    MedicationRepository medicationRepository;

    ShipmentMapper mapper;

    @Override
    public ShipmentInfo getById(Long id) {
        log.info("Retrieving Shipment with relations from database, id: {}", id);
        return mapper.mapToDto(getOrThrow(shipmentRepository.findByIdFetchRelations(id), id));
    }

    @Override
    @Transactional
    public ShipmentInfo register(UploadShipmentRequest request) {
        log.info("Registering a Shipment, request: {}", request);
        Shipment shipment = mapper.mapToShipment(request);
        shipment.setDrone(droneRepository.getReferenceById(request.droneId()));
        shipment.setMedications(request.medicationIds().stream()
                .map(medicationRepository::getReferenceById)
                .toList());
        shipment = shipmentRepository.save(shipment);
        ShipmentInfo result = mapper.mapToDto(shipment);
        log.info("Shipment was successfully registered, request: {}", request);
        return result;
    }

    @Override
    @Transactional
    public ShipmentInfo update(Long id, UploadShipmentRequest request) {
        log.info("Updating Shipment, id: {}", id);
        Shipment shipment = getOrThrow(shipmentRepository.findById(id), id);
        shipment = shipmentRepository.save(merge(shipment, request));
        ShipmentInfo result = mapper.mapToDto(shipment);
        log.info("Shipment was successfully updated! Request: {}", request);
        return result;
    }

    @Override
    @Transactional
    public void delete(Long id) {
        log.info("Deleting Shipment, id: {}", id);
        shipmentRepository.delete(getOrThrow(shipmentRepository.findById(id), id));
        log.info("Shipment with id = {} was successfully deleted!", id);
    }

    private Shipment merge(Shipment shipment, UploadShipmentRequest request) {
        if (request.droneId() != null) {
            shipment.setDrone(droneRepository.getReferenceById(request.droneId()));
        }
        if (request.status() != null) shipment.setStatus(request.status());
        if (request.details() != null) shipment.setDetails(request.details());
        if (request.medicationIds() != null && !request.medicationIds().isEmpty()) {
            shipment.setMedications(request.medicationIds().stream()
                    .map(medicationRepository::getReferenceById)
                    .toList());
        }
        return shipment;
    }

    private Shipment getOrThrow(Optional<Shipment> optional, Long id) {
        return optional.orElseThrow(() -> new NotFoundException("No Shipment with id = " + id + " was found"));
    }

}
