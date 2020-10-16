package com.persoff68.fatodo.service.exception;

import com.persoff68.fatodo.exception.AbstractException;
import org.springframework.http.HttpStatus;

public class WrongPasswordException extends AbstractException {
    private static final String MESSAGE = "Wrong current password";
    private static final String FEEDBACK_CODE = "auth.wrongPassword";

    public WrongPasswordException() {
        super(HttpStatus.BAD_REQUEST, MESSAGE, FEEDBACK_CODE);
    }

}
