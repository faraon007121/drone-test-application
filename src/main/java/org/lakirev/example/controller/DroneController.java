package org.lakirev.example.controller;

import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.lakirev.example.model.Coordinates;
import org.lakirev.example.model.DroneState;
import org.lakirev.example.model.request.UploadDroneRequest;
import org.lakirev.example.model.response.DroneFullInfo;
import org.lakirev.example.model.response.DroneShortInfo;
import org.lakirev.example.service.DroneManager;
import org.lakirev.example.service.DroneService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequestMapping("drone")
public class DroneController {

    DroneService service;

    DroneManager droneManager;

    @GetMapping("/{id}")
    public DroneShortInfo getById(@PathVariable Long id) {
        return service.getById(id);
    }

    @GetMapping("/{id}/full")
    public DroneFullInfo getFullById(@PathVariable Long id) {
        return service.getFullById(id);
    }

    @GetMapping("/available")
    public List<DroneState> getAvailableDrones() {
        return droneManager.getAvailableDrones();
    }

    @GetMapping("/{id}/state")
    public DroneState getDroneState(@PathVariable Long id) {
        return droneManager.getDroneState(id);
    }

    @PostMapping("/{id}/connect")
    public void connect(@PathVariable Long id) {
        droneManager.connect(id);
    }

    @PostMapping("/{id}/disconnect")
    public void disconnect(@PathVariable Long id) {
        droneManager.disconnect(id);
    }

    @PostMapping("/{id}/load")
    public void loadMedications(@PathVariable Long id, @RequestBody List<Long> medicationIds) {
        droneManager.loadDroneWithMedications(id, medicationIds);
    }

    @PostMapping("/{id}/unload")
    public void unloadMedications(@PathVariable Long id) {
        droneManager.unloadMedications(id);
    }

    @PostMapping("/{id}/destination")
    public void setDestination(@PathVariable Long id, @RequestBody Coordinates destination) {
        droneManager.setDestination(id, destination);
    }

    @PostMapping("/{id}/launch")
    public void launchDrone(@PathVariable Long id) {
        droneManager.launchDrone(id);
    }

    @PostMapping("/register")
    public ResponseEntity<DroneShortInfo> register(@Valid @RequestBody UploadDroneRequest request) {
        return new ResponseEntity<>(service.register(request), HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public DroneShortInfo update(@PathVariable Long id, @Valid @RequestBody UploadDroneRequest request) {
        return service.update(id, request);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        service.delete(id);
    }

}
