package tv.mopl.common.exception;

import java.util.Map;

public final class DuplicateException extends BusinessException {

    public DuplicateException(ErrorCode errorCode) {
        super(errorCode);
    }

    public DuplicateException(ErrorCode errorCode, String message) {
        super(errorCode, message);
    }

    public DuplicateException(ErrorCode errorCode, Map<String, Object> details) {
        super(errorCode, details);
    }

    public DuplicateException(ErrorCode errorCode, String message, Map<String, Object> details) {
        super(errorCode, message, details);
    }
}
