package nl.hu.userservice.application.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import static nl.hu.userservice.application.exception.ErrorResponseFactory.createResponse;

/**
 * This class is used to handle exceptions thrown by the controllers.
 * It will return a response entity with a status code when one of the exceptions is thrown.
 */
@ControllerAdvice
public class ControllerAdvisor extends ResponseEntityExceptionHandler {

    @ExceptionHandler(NoDataFoundException.class)
    public ResponseEntity<Object> handleNoDataFoundException(NoDataFoundException ex, WebRequest request) {
        return createResponse(ex, request, HttpStatus.OK);
    }

    @ExceptionHandler(AlreadyExistException.class)
    public ResponseEntity<Object> handleExistException(AlreadyExistException ex, WebRequest request) {
        return createResponse(ex, request, HttpStatus.CONFLICT);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<Object> generalExceptionHandler(NoDataFoundException ex, WebRequest request) {
        return createResponse(ex, request, HttpStatus.BAD_REQUEST);
    }


}
