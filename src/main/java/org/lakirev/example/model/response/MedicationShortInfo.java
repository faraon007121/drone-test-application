package org.lakirev.example.model.response;

public record MedicationShortInfo(
        Long id,
        String name,
        Integer weight,
        String code) {
}
