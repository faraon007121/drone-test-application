package org.lakirev.example.model.response;

public record ArgumentError(String argument, Object value, String message) {
}