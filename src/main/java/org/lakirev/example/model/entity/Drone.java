package org.lakirev.example.model.entity;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.OneToMany;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.FieldDefaults;
import lombok.experimental.SuperBuilder;
import org.lakirev.example.model.DroneModel;

import java.util.List;

@Entity
@SuperBuilder
@NoArgsConstructor
@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Drone extends AbstractEntity {

    @Column(length = 100, nullable = false, unique = true)
    String serialNumber;

    @Enumerated(EnumType.STRING)
    @Column(length = 50, nullable = false)
    DroneModel model;

    @Column(nullable = false)
    Integer weightLimit;

    @OneToMany(mappedBy = "drone", cascade = CascadeType.ALL)
    List<Shipment> shipments;

}
