package UserService.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

import java.time.Instant;

@Getter
public class BaseException extends RuntimeException {
    private final HttpStatus httpStatus;
    private final Instant timestamp;

    public BaseException(String message, HttpStatus httpStatus) {
        super(message);
        this.httpStatus = httpStatus;
        this.timestamp = Instant.now();
    }

}
