package com.persoff68.fatodo.service.exception;

import com.persoff68.fatodo.exception.AbstractException;
import org.springframework.http.HttpStatus;

public final class UserAlreadyActivatedException extends AbstractException {
    private static final String MESSAGE = "User already activated";

    public UserAlreadyActivatedException() {
        super(HttpStatus.CONFLICT, MESSAGE);
    }

}
