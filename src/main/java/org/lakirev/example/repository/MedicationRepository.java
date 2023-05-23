package org.lakirev.example.repository;

import org.lakirev.example.model.entity.Medication;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface MedicationRepository extends JpaRepository<Medication, Long> {

    boolean existsByCode(String code);

    @Query("select m from Medication m join fetch m.shipments where m.id = :id")
    Optional<Medication> findByIdFetchRelations(@Param("id") Long id);

    List<Medication> findByIdIn(List<Long> ids);

}