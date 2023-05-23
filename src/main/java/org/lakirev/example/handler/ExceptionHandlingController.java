package org.lakirev.example.handler;

import lombok.extern.slf4j.Slf4j;
import org.lakirev.example.exception.drone.DroneException;
import org.lakirev.example.exception.NotFoundException;
import org.lakirev.example.model.response.ArgumentError;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.lakirev.example.exception.BadRequestException;
import org.lakirev.example.model.response.ErrorResponse;
import org.springframework.web.multipart.MultipartException;

import java.util.ArrayList;
import java.util.List;

@ControllerAdvice
@Slf4j
public class ExceptionHandlingController {

    @ExceptionHandler(BadRequestException.class)
    public ResponseEntity<ErrorResponse> handleBadRequestException(BadRequestException e) {
        ErrorResponse errorResponse = new ErrorResponse("Bad request error", e.getMessage());
        log.error(errorResponse.toString());
        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleValidationException(MethodArgumentNotValidException e) {
        List<ArgumentError> argumentErrors = new ArrayList<>();
        for (FieldError fieldError : e.getFieldErrors()) {
            argumentErrors.add(new ArgumentError(fieldError.getField(), fieldError.getRejectedValue(), fieldError.getDefaultMessage()));
        }
        ErrorResponse errorResponse = new ErrorResponse("Controller argument validation error", argumentErrors);
        log.error(errorResponse.toString());
        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<ErrorResponse> handleDataIntegrityException(DataIntegrityViolationException e) {
        ErrorResponse errorResponse = new ErrorResponse("Data integrity exception", e.getMessage());
        log.error(errorResponse.toString());
        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(MultipartException.class)
    public ResponseEntity<ErrorResponse> handleMultipartException(MultipartException e) {
        ErrorResponse errorResponse = new ErrorResponse("Multipart exception", e.getMessage());
        log.error(errorResponse.toString());
        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(DroneException.class)
    public ResponseEntity<ErrorResponse> handleDroneException(DroneException e) {
        ErrorResponse errorResponse = new ErrorResponse("Drone exception", e.getMessage());
        log.error(errorResponse.toString());
        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ErrorResponse> handleIllegalArgumentException(IllegalArgumentException e) {
        ErrorResponse errorResponse = new ErrorResponse("Drone exception", e.getMessage());
        log.error(errorResponse.toString());
        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<ErrorResponse> handleNotFoundException(NotFoundException e) {
        ErrorResponse errorResponse = new ErrorResponse("Not found exception", e.getMessage());
        log.error(errorResponse.toString());
        return new ResponseEntity<>(errorResponse, HttpStatus.NOT_FOUND);
    }

}
