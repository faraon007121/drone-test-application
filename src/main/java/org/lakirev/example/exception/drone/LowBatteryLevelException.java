package org.lakirev.example.exception.drone;

public class LowBatteryLevelException extends DroneException {

    public LowBatteryLevelException(String message) {
        super(message);
    }

}
