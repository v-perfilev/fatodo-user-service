package com.persoff68.fatodo.service.exception;

import org.zalando.problem.Status;

public final class ModelAlreadyExistsProblem extends AbstractDatabaseProblem {

    public ModelAlreadyExistsProblem() {
        super(Status.BAD_REQUEST, "Model already exits in database");
    }

}
