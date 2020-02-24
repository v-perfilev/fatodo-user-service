package com.persoff68.fatodo.exception.handler;

import com.persoff68.fatodo.exception.constant.ExceptionTypes;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataAccessException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.NativeWebRequest;
import org.zalando.problem.Problem;
import org.zalando.problem.Status;

@ControllerAdvice
@RequiredArgsConstructor
public class ExceptionHandling {

    private final ExceptionTranslator exceptionTranslator;

    @ExceptionHandler
    public ResponseEntity<Problem> handleMongoException(DataAccessException e, NativeWebRequest request) {
        Problem problem = Problem.builder()
                .withType(ExceptionTypes.DB_TYPE)
                .withTitle(e.getMessage())
                .withStatus(Status.BAD_REQUEST)
                .build();
        return exceptionTranslator.create(e, problem, request);
    }

}
