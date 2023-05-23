package org.lakirev.example.mapper;

import org.lakirev.example.annotation.MapstructIgnore;
import org.lakirev.example.model.entity.Shipment;
import org.lakirev.example.model.request.UploadShipmentRequest;
import org.lakirev.example.model.response.ShipmentInfo;
import org.mapstruct.BeanMapping;
import org.mapstruct.Context;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring")
public interface ShipmentMapper {

    @Mapping(source = "destinationLatitude", target = "destination.latitude")
    @Mapping(source = "destinationLongitude", target = "destination.longitude")
    ShipmentInfo mapToDto(Shipment shipment, @Context CycleAvoidingMapperContext context);

    @BeanMapping(unmappedTargetPolicy = ReportingPolicy.IGNORE)
    @Mapping(source = "destination.latitude", target = "destinationLatitude")
    @Mapping(source = "destination.longitude", target = "destinationLongitude")
    Shipment mapToShipment(UploadShipmentRequest request);

    @MapstructIgnore
    default ShipmentInfo mapToDto(Shipment shipment) {
        return mapToDto(shipment, new CycleAvoidingMapperContext());
    }

}
