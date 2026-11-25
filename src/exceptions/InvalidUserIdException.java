package exceptions;

public class InvalidUserIdException extends AccountException {

    public InvalidUserIdException(String message) {
        super(message);
    }
}
