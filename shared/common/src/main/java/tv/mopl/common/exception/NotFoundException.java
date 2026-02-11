package tv.mopl.common.exception;

import java.util.Map;

public final class NotFoundException extends BusinessException {

    public NotFoundException(ErrorCode errorCode) {
        super(errorCode);
    }

    public NotFoundException(ErrorCode errorCode, String message) {
        super(errorCode, message);
    }

    public NotFoundException(ErrorCode errorCode, Map<String, Object> details) {
        super(errorCode, details);
    }

    public NotFoundException(ErrorCode errorCode, String message, Map<String, Object> details) {
        super(errorCode, message, details);
    }
}
