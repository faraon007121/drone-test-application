package org.lakirev.example.service;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.lakirev.example.exception.NotFoundException;
import org.lakirev.example.mapper.MedicationMapper;
import org.lakirev.example.model.response.MedicationFullInfo;
import org.lakirev.example.model.response.MedicationShortInfo;
import org.lakirev.example.repository.MedicationRepository;
import org.springframework.stereotype.Service;
import org.lakirev.example.exception.AlreadyExistsException;
import org.lakirev.example.model.entity.Medication;
import org.lakirev.example.model.request.UploadMedicationRequest;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class DefaultMedicationService implements MedicationService {

    MedicationRepository repository;

    MedicationMapper mapper;

    @Override
    public MedicationShortInfo getById(Long id) {
        log.info("Retrieving Medication from database, id: {}", id);
        return mapper.mapToMedicationShortInfo(getOrThrow(repository.findById(id), id));
    }

    @Override
    public List<MedicationShortInfo> getByIdIn(List<Long> ids) {
        log.info("Retrieving Medications from database, ids: " + ids);
        return repository.findByIdIn(ids)
                .stream()
                .map(mapper::mapToMedicationShortInfo)
                .collect(Collectors.toList());
    }

    @Override
    public MedicationFullInfo getFullById(Long id) {
        log.info("Retrieving Medication with relations from database, id: {}", id);
        return mapper.mapToMedicationFullInfo(getOrThrow(repository.findByIdFetchRelations(id), id));
    }

    @Override
    public byte[] getImage(Long id) {
        log.info("Retrieving medication image, id: {}", id);
        Medication medication = getOrThrow(repository.findById(id), id);
        if (medication.getImage() == null) {
            throw new NotFoundException("Image of medication with id = " + id + " was not found");
        }
        return medication.getImage();
    }

    @Override
    @Transactional
    public MedicationShortInfo register(UploadMedicationRequest request) {
        log.info("Registering a Medication, request: {}", request);
        if (repository.existsByCode(request.code())) {
            throw new AlreadyExistsException("Medication with code = " + request.code() + " already exists");
        }
        Medication medication = repository.save(mapper.mapToMedication(request));
        MedicationShortInfo result = mapper.mapToMedicationShortInfo(medication);
        log.info("Medication with code = {} was successfully registered!", request.code());
        return result;
    }

    @Override
    @Transactional
    public MedicationShortInfo update(Long id, UploadMedicationRequest request) {
        log.info("Updating Medication, id: {}", id);
        Medication medication = getOrThrow(repository.findById(id), id);
        medication = repository.save(mapper.merge(medication, request));
        MedicationShortInfo result = mapper.mapToMedicationShortInfo(medication);
        log.info("Medication was successfully updated! Request: {}", request);
        return result;
    }

    @Override
    public void uploadImage(Long id, byte[] image) {
        log.info("Uploading image, medicationId: {}", id);
        Medication medication = getOrThrow(repository.findById(id), id);
        medication.setImage(image);
        repository.save(medication);
        log.info("Image for medication with id = {} was successfully uploaded", id);
    }

    @Override
    @Transactional
    public void delete(Long id) {
        log.info("Deleting Medication, id: {}", id);
        repository.delete(getOrThrow(repository.findById(id), id));
        log.info("Medication with id = {} was successfully deleted!", id);
    }

    private Medication getOrThrow(Optional<Medication> optional, Long id) {
        return optional.orElseThrow(() -> new NotFoundException("No Medication with id = " + id + " was found"));
    }

}
