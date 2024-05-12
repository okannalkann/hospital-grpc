package com.hospitalservice.exception;

public class HospitalSaveException extends RuntimeException {
    public HospitalSaveException(String message, Throwable cause) {
        super(message, cause);
    }
}
