package pl.mb.githubauth.exceptions;

public class FailureException extends RuntimeException {
    public FailureException(String message) {
        super(message);
    }
}
