package org.lakirev.example.exception.drone;

public class InsufficientDroneStateException extends DroneException {

    public InsufficientDroneStateException(String message) {
        super(message);
    }

}
