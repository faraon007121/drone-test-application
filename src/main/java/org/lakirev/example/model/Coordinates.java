package org.lakirev.example.model;

import jakarta.validation.constraints.NotNull;

public record Coordinates(@NotNull Double latitude, @NotNull Double longitude) {
}
