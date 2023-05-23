package org.lakirev.example.service;

import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.lakirev.example.data.MedicationTestData;
import org.lakirev.example.exception.AlreadyExistsException;
import org.lakirev.example.exception.NotFoundException;
import org.lakirev.example.mapper.MedicationMapper;
import org.lakirev.example.mapper.MedicationMapperImpl;
import org.lakirev.example.mapper.ShipmentMapperImpl;
import org.lakirev.example.model.entity.Medication;
import org.lakirev.example.model.request.UploadMedicationRequest;
import org.lakirev.example.model.response.MedicationShortInfo;
import org.lakirev.example.repository.MedicationRepository;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;

@FieldDefaults(level = AccessLevel.PRIVATE)
@ExtendWith(MockitoExtension.class)
class DefaultMedicationServiceTest {

    @InjectMocks
    DefaultMedicationService service;

    @Spy
    MedicationMapper mapper = new MedicationMapperImpl(new ShipmentMapperImpl());

    @Mock
    MedicationRepository repository;

    UploadMedicationRequest testRequest;

    MedicationShortInfo testMedicationShortInfo;

    @BeforeEach
    void init() {
        testRequest = MedicationTestData.getUploadRequest();
        testMedicationShortInfo = MedicationTestData.getMedicationShortInfoList().get(0);
    }

    @Test
    void testRegisterWhenDoesNotExist() {
        Mockito.when(repository.save(any())).then(args -> {
            Object argument = args.getArgument(0);
            Medication medication = (Medication) argument;
            medication.setId(1L);
            return argument;
        });
        MedicationShortInfo actual = service.register(testRequest);
        Assertions.assertEquals(testMedicationShortInfo, actual);
    }

    @Test
    void testRegisterWhenExists() {
        Mockito.when(repository.existsByCode(testRequest.code())).thenReturn(true);
        Assertions.assertThrows(AlreadyExistsException.class, () -> service.register(testRequest));
    }

    @Test
    void testNotFoundThrowing() {
        Mockito.when(repository.findById(1L)).thenReturn(Optional.empty());
        Mockito.when(repository.findByIdFetchRelations(1L)).thenReturn(Optional.empty());
        Assertions.assertThrows(NotFoundException.class, () -> service.getById(1L));
        Assertions.assertThrows(NotFoundException.class, () -> service.getFullById(1L));
        Assertions.assertThrows(NotFoundException.class, () -> service.update(1L, testRequest));
        Assertions.assertThrows(NotFoundException.class, () -> service.delete(1L));
    }

}