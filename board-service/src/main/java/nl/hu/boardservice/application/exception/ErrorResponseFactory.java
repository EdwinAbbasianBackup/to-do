package nl.hu.boardservice.application.exception;

import nl.hu.boardservice.application.exception.ControllerAdvisor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.context.request.WebRequest;

import java.time.LocalDateTime;
import java.util.LinkedHashMap;
import java.util.Map;

public abstract class ErrorResponseFactory {
    private static final Logger logger = LogManager.getLogger(ControllerAdvisor.class);


    /**
     * Creates a standardized error response.
     *
     * @param ex      The exception that was thrown.
     * @param request The web request that caused the exception.
     * @param status  The HTTP status to be returned.
     * @return A ResponseEntity containing the error details.
     */
    protected static ResponseEntity<Object> createResponse(Exception ex, WebRequest request, HttpStatus status) {
        logger.error("Error occurred: {}", ex.getMessage(), ex);

        Map<String, Object> body = new LinkedHashMap<>();
        body.put("timestamp", LocalDateTime.now());
        body.put("message", ex.getMessage());
        body.put("request", request.getDescription(false));

        return new ResponseEntity<>(body, status);
    }
}
