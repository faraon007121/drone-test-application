package org.lakirev.example.controller;

import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.lakirev.example.model.request.UploadMedicationRequest;
import org.lakirev.example.model.response.MedicationFullInfo;
import org.lakirev.example.model.response.MedicationShortInfo;
import org.lakirev.example.service.MedicationService;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequestMapping("medication")
public class MedicationController {

    MedicationService service;

    @GetMapping("/{id}")
    public MedicationShortInfo getById(@PathVariable Long id) {
        return service.getById(id);
    }

    @GetMapping("/{id}/full")
    public MedicationFullInfo getFullById(@PathVariable Long id) {
        return service.getFullById(id);
    }

    @GetMapping(value = "/{id}/image", produces = MediaType.IMAGE_JPEG_VALUE)
    public byte[] getImage(@PathVariable Long id) {
        return service.getImage(id);
    }

    @PostMapping("/register")
    public ResponseEntity<MedicationShortInfo> register(@Valid @RequestBody UploadMedicationRequest request) {
        return new ResponseEntity<>(service.register(request), HttpStatus.CREATED);
    }

    @PutMapping(value = "/{id}")
    public MedicationShortInfo update(@PathVariable Long id, @Valid @RequestBody UploadMedicationRequest request) {
        return service.update(id, request);
    }

    @PutMapping(value = "/{id}/image")
    public void uploadImage(@PathVariable Long id, @RequestParam("image") MultipartFile image) throws IOException {
        service.uploadImage(id, image.getBytes());
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        service.delete(id);
    }

}
