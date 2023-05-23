package org.lakirev.example.model.entity;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.ManyToMany;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.FieldDefaults;
import lombok.experimental.SuperBuilder;

import java.util.List;

@Entity
@SuperBuilder
@NoArgsConstructor
@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Medication extends AbstractEntity {

    @Column(nullable = false)
    String name;

    @Column(nullable = false)
    Integer weight;

    @Column(nullable = false, unique = true)
    String code;

    byte[] image;

    @ManyToMany(mappedBy = "medications", cascade = CascadeType.ALL)
    List<Shipment> shipments;

}
