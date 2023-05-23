package org.lakirev.example.mapper;

import org.lakirev.example.model.response.MedicationShortInfo;
import org.mapstruct.BeanMapping;
import org.mapstruct.Context;
import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;
import org.lakirev.example.annotation.MapstructIgnore;
import org.lakirev.example.model.response.MedicationFullInfo;
import org.lakirev.example.model.entity.Medication;
import org.lakirev.example.model.request.UploadMedicationRequest;

@Mapper(componentModel = "spring",
        injectionStrategy = InjectionStrategy.CONSTRUCTOR,
        uses = ShipmentMapper.class)
public interface MedicationMapper {

    MedicationFullInfo mapToMedicationFullInfo(Medication medication, @Context CycleAvoidingMapperContext context);

    @BeanMapping(unmappedTargetPolicy = ReportingPolicy.IGNORE)
    Medication mapToMedication(UploadMedicationRequest request);

    MedicationShortInfo mapToMedicationShortInfo(Medication medication);

    @MapstructIgnore
    default MedicationFullInfo mapToMedicationFullInfo(Medication medication) {
        return mapToMedicationFullInfo(medication, new CycleAvoidingMapperContext());
    }

    default Medication merge(Medication medication, UploadMedicationRequest request) {
        if (request.name() != null) medication.setName(request.name());
        if (request.code() != null) medication.setCode(request.code());
        if (request.weight() != null) medication.setWeight(request.weight());
        if (request.image() != null) medication.setImage(request.image());
        return medication;
    }

}
