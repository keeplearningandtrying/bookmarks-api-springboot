package com.sivalabs.bookmarks.config.advice;

import com.sivalabs.bookmarks.domain.exceptions.BadRequestException;
import com.sivalabs.bookmarks.domain.exceptions.BookmarksException;
import com.sivalabs.bookmarks.domain.exceptions.ResourceNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.NativeWebRequest;
import org.zalando.problem.Problem;
import org.zalando.problem.Status;
import org.zalando.problem.spring.web.advice.ProblemHandling;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    private final ProblemHandling translator = new ProblemHandling() {};

    @ExceptionHandler(ResourceNotFoundException.class)
    ResponseEntity<Problem> handleResourceNotFoundException(
            ResourceNotFoundException exception, NativeWebRequest request) {
        log.error(exception.getLocalizedMessage(), exception);
        return translator.create(Status.NOT_FOUND, exception, request);
    }

    @ExceptionHandler(BookmarksException.class)
    ResponseEntity<Problem> handleDevZoneException(
            BookmarksException exception, NativeWebRequest request) {
        log.error(exception.getLocalizedMessage(), exception);
        return translator.create(Status.BAD_REQUEST, exception, request);
    }

    @ExceptionHandler(BadRequestException.class)
    ResponseEntity<Problem> handleBadRequestException(
            BadRequestException exception, NativeWebRequest request) {
        log.error(exception.getLocalizedMessage(), exception);
        return translator.create(Status.BAD_REQUEST, exception, request);
    }
}
