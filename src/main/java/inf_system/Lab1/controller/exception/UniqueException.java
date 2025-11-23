package inf_system.Lab1.controller.exception;

public class UniqueException extends RuntimeException {
    public UniqueException(String message) {
        super(message);
    }
}