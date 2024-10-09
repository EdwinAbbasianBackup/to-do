package nl.hu.boardservice.application.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import static nl.hu.boardservice.application.exception.ErrorResponseFactory.createResponse;

/**
 * This class is used to handle exceptions thrown by the controllers.
 * It will return a response entity with a status code when one of the exceptions is thrown.
 */
@ControllerAdvice
public class ControllerAdvisor extends ResponseEntityExceptionHandler {


    /**
     * @param ex      The exception thrown by the controller.
     * @param request The request that caused the exception.
     * @return A response entity with a status code (200) as the request sends an empty array or data and a message.
     */
    @ExceptionHandler(NoDataFoundException.class)
    public ResponseEntity<Object> handleNoDataFoundException(NoDataFoundException ex, WebRequest request) {
        return createResponse(ex, request, HttpStatus.OK);
    }

    /**
     * @param ex      The exception thrown by the controller.
     * @param request The request that caused the exception.
     * @return A response entity with a status code (504) as the request sends an empty array or data and a message.
     */
    @ExceptionHandler(ServerErrorException.class)
    public ResponseEntity<Object> handleTimeOutError(ServerErrorException ex, WebRequest request) {
        return createResponse(ex, request, HttpStatus.GATEWAY_TIMEOUT);
    }




}
