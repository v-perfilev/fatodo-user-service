package com.persoff68.fatodo.service.exception;

import org.zalando.problem.Status;

public final class CommonDatabaseProblem extends AbstractDatabaseProblem {

    public CommonDatabaseProblem(String detail) {
        super(Status.BAD_REQUEST, detail);
    }

}
