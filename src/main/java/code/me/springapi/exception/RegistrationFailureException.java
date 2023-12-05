package code.me.springapi.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.BAD_REQUEST)
public class RegistrationFailureException extends RuntimeException {
    public RegistrationFailureException(String message) {
        super(message);
    }
}