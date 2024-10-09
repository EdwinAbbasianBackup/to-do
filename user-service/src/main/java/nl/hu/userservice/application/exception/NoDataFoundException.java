package nl.hu.userservice.application.exception;

public class NoDataFoundException extends NullPointerException {
    public NoDataFoundException(String message) {
        super(message);
    }
}

