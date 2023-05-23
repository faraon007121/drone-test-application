package org.lakirev.example.repository;

import org.lakirev.example.model.entity.Drone;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface DroneRepository extends JpaRepository<Drone, Long> {

    boolean existsBySerialNumber(String serialNumber);

    @Query("select d from Drone d join fetch d.shipments s where d.id = :id")
    Optional<Drone> findByIdFetchRelations(@Param("id") Long id);

}