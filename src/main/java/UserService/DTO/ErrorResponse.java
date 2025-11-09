package UserService.DTO;

import UserService.exception.BaseException;
import lombok.Getter;
import org.springframework.http.HttpStatus;

import java.time.Instant;
import java.util.List;

@Getter
public class ErrorResponse {

    private final Instant timestamp;
    private final String message;
    private final HttpStatus status;
    private final List<String> details;

    public ErrorResponse(Instant timestamp, String message, HttpStatus status, List<String> details) {
        this.timestamp = timestamp;
        this.message = message;
        this.status = status;
        this.details = details;
    }

    public static ErrorResponse of(BaseException baseException) {
        return new ErrorResponse(
                baseException.getTimestamp(),
                baseException.getMessage(),
                baseException.getHttpStatus(),
                null
        );
    }
}
