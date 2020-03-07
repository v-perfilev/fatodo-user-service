package com.persoff68.fatodo.service.exception;

import com.persoff68.fatodo.exception.constant.ExceptionTypes;
import org.zalando.problem.AbstractThrowableProblem;
import org.zalando.problem.Status;

public abstract class AbstractDatabaseProblem extends AbstractThrowableProblem {

    public AbstractDatabaseProblem(Status status, String detail) {
        super(ExceptionTypes.DB_TYPE, "Database error", status, detail);
    }

}
