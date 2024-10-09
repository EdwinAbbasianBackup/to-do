package nl.hu.boardservice.application.exception;

public class NoDataFoundException extends NullPointerException {
    public NoDataFoundException(String message) {
        super(message);
    }
}

