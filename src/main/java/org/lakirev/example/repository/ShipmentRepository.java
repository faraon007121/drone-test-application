package org.lakirev.example.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.lakirev.example.model.entity.Shipment;

import java.util.Optional;

@Repository
public interface ShipmentRepository extends JpaRepository<Shipment, Long> {

    @Query("select s from Shipment s join fetch s.medications where s.id = :id")
    Optional<Shipment> findByIdFetchRelations(@Param("id") Long id);

}
