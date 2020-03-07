package com.persoff68.fatodo.service.exception.handler;

import com.persoff68.fatodo.exception.handler.ExceptionTranslator;
import com.persoff68.fatodo.service.exception.CommonDatabaseProblem;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataAccessException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.zalando.problem.Problem;
import org.zalando.problem.ThrowableProblem;

import javax.servlet.http.HttpServletRequest;

@ControllerAdvice
@RequiredArgsConstructor
public class ServiceExceptionHandling {

    private final ExceptionTranslator exceptionTranslator;

    @ExceptionHandler(DataAccessException.class)
    public ResponseEntity<Problem> handleDatabaseException(Exception e, HttpServletRequest request) {
        ThrowableProblem problem = new CommonDatabaseProblem(e.getMessage());
        return exceptionTranslator.process(problem, request.getRequestURI());
    }

}
