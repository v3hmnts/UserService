package UserService.exception;

import org.springframework.http.HttpStatus;

public class BusinessRuleConstraintViolationException extends BaseException {

    public BusinessRuleConstraintViolationException(String violationMessage) {
        super(violationMessage,
                HttpStatus.BAD_REQUEST);
    }

}
