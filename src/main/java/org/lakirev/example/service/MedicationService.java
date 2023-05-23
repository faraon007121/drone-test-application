package org.lakirev.example.service;

import org.lakirev.example.model.response.MedicationFullInfo;
import org.lakirev.example.model.response.MedicationShortInfo;
import org.lakirev.example.model.request.UploadMedicationRequest;

import java.util.List;
import java.util.Set;

public interface MedicationService {

    MedicationShortInfo getById(Long id);

    List<MedicationShortInfo> getByIdIn(List<Long> ids);

    MedicationFullInfo getFullById(Long id);

    byte[] getImage(Long id);

    MedicationShortInfo register(UploadMedicationRequest request);

    MedicationShortInfo update(Long id, UploadMedicationRequest request);

    void uploadImage(Long id, byte[] image);

    void delete(Long id);

}