package com.persoff68.fatodo.service.exception;

import org.zalando.problem.Status;

public final class ModelNotFoundProblem extends AbstractDatabaseProblem {

    public ModelNotFoundProblem() {
        super(Status.NOT_FOUND, "Model not found in database");
    }

}
