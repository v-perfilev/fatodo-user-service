package com.persoff68.fatodo.service.exception;

import com.persoff68.fatodo.exception.AbstractException;
import org.springframework.http.HttpStatus;

public class WrongProviderException extends AbstractException {
    private static final String MESSAGE = "Wrong provider";
    private static final String FEEDBACK_CODE = "auth.wrongProvider";

    public WrongProviderException() {
        super(HttpStatus.BAD_REQUEST, MESSAGE, FEEDBACK_CODE);
    }

}
