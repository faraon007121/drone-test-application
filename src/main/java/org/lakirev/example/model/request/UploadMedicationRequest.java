package org.lakirev.example.model.request;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Positive;

public record UploadMedicationRequest(

        @NotNull
        @Pattern(regexp = "[a-zA-Z0-9_-]*", message = "Name must contain only letters, numbers, underscores and dashes")
        String name,

        @NotNull
        @Positive(message = "Weight must be positive")
        Integer weight,

        @NotNull
        @Pattern(regexp = "[A-Z0-9_]*", message = "Code must contain only upper case letters, numbers and underscores")
        String code,

        byte[] image) {

}
