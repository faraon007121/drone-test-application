package org.lakirev.example.controller;

import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.lakirev.example.model.response.ShipmentInfo;
import org.lakirev.example.model.request.UploadShipmentRequest;
import org.lakirev.example.service.ShipmentService;
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

@RestController
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequestMapping("shipment")
public class ShipmentController {

    ShipmentService service;

    @GetMapping("/{id}")
    public ShipmentInfo getById(@PathVariable Long id) {
        return service.getById(id);
    }

    @PostMapping("/register")
    public ResponseEntity<ShipmentInfo> register(@Valid @RequestBody UploadShipmentRequest request) {
        return new ResponseEntity<>(service.register(request), HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ShipmentInfo update(@PathVariable Long id, @Valid @RequestBody UploadShipmentRequest request) {
        return service.update(id, request);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        service.delete(id);
    }

}
