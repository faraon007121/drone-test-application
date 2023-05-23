package org.lakirev.example.mapper;

import org.lakirev.example.model.response.DroneFullInfo;
import org.lakirev.example.model.response.DroneShortInfo;
import org.mapstruct.BeanMapping;
import org.mapstruct.Context;
import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;
import org.lakirev.example.annotation.MapstructIgnore;
import org.lakirev.example.model.entity.Drone;
import org.lakirev.example.model.request.UploadDroneRequest;

@Mapper(componentModel = "spring",
        injectionStrategy = InjectionStrategy.CONSTRUCTOR,
        uses = ShipmentMapper.class)
public interface DroneMapper {

    DroneFullInfo mapToDroneFullInfo(Drone drone, @Context CycleAvoidingMapperContext context);

    @BeanMapping(unmappedTargetPolicy = ReportingPolicy.IGNORE)
    Drone mapToDrone(UploadDroneRequest request);

    DroneShortInfo mapToDroneShortInfo(Drone drone);

    @MapstructIgnore
    default DroneFullInfo mapToDroneFullInfo(Drone drone) {
        return mapToDroneFullInfo(drone, new CycleAvoidingMapperContext());
    }

    default Drone merge(Drone drone, UploadDroneRequest request) {
        if (request.serialNumber() != null) drone.setSerialNumber(request.serialNumber());
        if (request.model() != null) drone.setModel(request.model());
        if (request.weightLimit() != null) drone.setWeightLimit(request.weightLimit());
        return drone;
    }

}
