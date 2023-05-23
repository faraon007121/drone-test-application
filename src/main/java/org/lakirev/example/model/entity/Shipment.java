package org.lakirev.example.model.entity;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.FieldDefaults;
import lombok.experimental.SuperBuilder;
import org.lakirev.example.model.ShipmentStatus;

import java.util.List;

@Entity
@SuperBuilder
@NoArgsConstructor
@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Shipment extends AbstractEntity {

    @ManyToOne
    @JoinColumn(nullable = false)
    Drone drone;

    @ManyToMany(cascade = {CascadeType.PERSIST})
    @JoinTable(
            joinColumns = @JoinColumn(name = "shipment_id", nullable = false),
            inverseJoinColumns = @JoinColumn(name = "medication_id", nullable = false))
    List<Medication> medications;

    @Column(nullable = false)
    Double destinationLatitude;

    @Column(nullable = false)
    Double destinationLongitude;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    ShipmentStatus status;

    String details;

}
