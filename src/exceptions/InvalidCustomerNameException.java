package exceptions;

public class InvalidCustomerNameException extends AccountException {

    public InvalidCustomerNameException(String message) {
        super(message);
    }
}
