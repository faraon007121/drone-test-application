package org.lakirev.example.service;

import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.lakirev.example.data.DroneTestData;
import org.lakirev.example.exception.AlreadyExistsException;
import org.lakirev.example.exception.NotFoundException;
import org.lakirev.example.mapper.DroneMapper;
import org.lakirev.example.mapper.DroneMapperImpl;
import org.lakirev.example.mapper.ShipmentMapperImpl;
import org.lakirev.example.model.response.DroneShortInfo;
import org.lakirev.example.model.entity.Drone;
import org.lakirev.example.model.request.UploadDroneRequest;
import org.lakirev.example.repository.DroneRepository;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;

@FieldDefaults(level = AccessLevel.PRIVATE)
@ExtendWith(MockitoExtension.class)
class DefaultDroneServiceTest {

    @InjectMocks
    DefaultDroneService service;

    @Spy
    DroneMapper mapper = new DroneMapperImpl(new ShipmentMapperImpl());

    @Mock
    DroneRepository repository;

    UploadDroneRequest testRequest;

    DroneShortInfo testDroneShortInfo;

    @BeforeEach
    void init() {
        testRequest = DroneTestData.getUploadRequest();
        testDroneShortInfo = DroneTestData.getDroneShortInfo();
    }

    @Test
    void testRegisterWhenDoesNotExist() {
        Mockito.when(repository.save(any())).then(args -> {
            Object argument = args.getArgument(0);
            Drone drone = (Drone) argument;
            drone.setId(1L);
            return argument;
        });
        DroneShortInfo actual = service.register(testRequest);
        Assertions.assertEquals(testDroneShortInfo, actual);
    }

    @Test
    void testRegisterWhenExists() {
        Mockito.when(repository.existsBySerialNumber(testRequest.serialNumber())).thenReturn(true);
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