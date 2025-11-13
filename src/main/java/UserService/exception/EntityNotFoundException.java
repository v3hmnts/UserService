package UserService.exception;

import org.springframework.http.HttpStatus;

public class EntityNotFoundException extends BaseException {
    public EntityNotFoundException(String entityName, String entityId) {
        super(String.format("%s with id=%s not found", entityName, entityId),
                HttpStatus.NOT_FOUND);
    }
}
