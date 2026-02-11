package tv.mopl.common.exception;

import java.util.Map;

public final class InvalidValueException extends BusinessException {

    public InvalidValueException(ErrorCode errorCode) {
        super(errorCode);
    }

    public InvalidValueException(ErrorCode errorCode, String message) {
        super(errorCode, message);
    }

    public InvalidValueException(ErrorCode errorCode, Map<String, Object> details) {
        super(errorCode, details);
    }

    public InvalidValueException(ErrorCode errorCode, String message, Map<String, Object> details) {
        super(errorCode, message, details);
    }
}
