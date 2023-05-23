package org.lakirev.example.service;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.lakirev.example.exception.AlreadyExistsException;
import org.lakirev.example.exception.NotFoundException;
import org.lakirev.example.mapper.DroneMapper;
import org.lakirev.example.model.entity.Drone;
import org.lakirev.example.model.request.UploadDroneRequest;
import org.lakirev.example.model.response.DroneFullInfo;
import org.lakirev.example.model.response.DroneShortInfo;
import org.lakirev.example.repository.DroneRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@Slf4j
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class DefaultDroneService implements DroneService {

    DroneRepository repository;

    DroneMapper mapper;

    @Override
    public DroneShortInfo getById(Long id) {
        log.info("Retrieving Drone from database, id: {}", id);
        return mapper.mapToDroneShortInfo(getOrThrow(repository.findById(id), id));
    }

    @Override
    public DroneFullInfo getFullById(Long id) {
        log.info("Retrieving Drone with relations from database, id: {}", id);
        return mapper.mapToDroneFullInfo(getOrThrow(repository.findByIdFetchRelations(id), id));
    }

    @Override
    @Transactional
    public DroneShortInfo register(UploadDroneRequest request) {
        log.info("Registering a Drone, request: {}", request);
        if (repository.existsBySerialNumber(request.serialNumber())) {
            throw new AlreadyExistsException("Drone with serialNumber = " + request.serialNumber() + " already exists");
        }
        Drone drone = repository.save(mapper.mapToDrone(request));
        DroneShortInfo result = mapper.mapToDroneShortInfo(drone);
        log.info("Drone with serialNumber = {} was successfully registered!", request.serialNumber());
        return result;
    }

    @Override
    @Transactional
    public DroneShortInfo update(Long id, UploadDroneRequest request) {
        log.info("Updating a Drone, id: {}", id);
        Drone drone = getOrThrow(repository.findById(id), id);
        drone = repository.save(mapper.merge(drone, request));
        DroneShortInfo result = mapper.mapToDroneShortInfo(drone);
        log.info("Drone was successfully updated! Request: {}", request);
        return result;
    }

    @Override
    @Transactional
    public void delete(Long id) {
        log.info("Deleting a Drone, id: {}", id);
        repository.delete(getOrThrow(repository.findById(id), id));
        log.info("Drone with id = {} was successfully deleted!", id);
    }

    private Drone getOrThrow(Optional<Drone> optional, Long id) {
        return optional.orElseThrow(() -> new NotFoundException("No Drone with id = " + id + " was found"));
    }

}
