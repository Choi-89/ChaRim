package com.charim.kiosk.User.Exception;

public class PasswordEqualException extends RuntimeException {
    public PasswordEqualException(String message) {
        super(message);
    }
}
