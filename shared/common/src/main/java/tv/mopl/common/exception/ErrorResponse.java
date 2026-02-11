package tv.mopl.common.exception;

import java.time.Instant;
import java.util.Map;

public record ErrorResponse(
    String code,
    String message,
    int status,
    Instant timestamp,
    Map<String, Object> details) {

    public static ErrorResponse of(ErrorCode errorCode) {
        return new ErrorResponse(
            errorCode.getCode(),
            errorCode.getMessage(),
            errorCode.getStatus(),
            Instant.now(),
            Map.of());
    }

    public static ErrorResponse of(ErrorCode errorCode, String message) {
        return new ErrorResponse(errorCode.getCode(), message, errorCode.getStatus(), Instant.now(), Map.of());
    }

    public static ErrorResponse of(ErrorCode errorCode, Map<String, Object> details) {
        return new ErrorResponse(
            errorCode.getCode(),
            errorCode.getMessage(),
            errorCode.getStatus(),
            Instant.now(),
            details);
    }

    public static ErrorResponse of(ErrorCode errorCode, String message, Map<String, Object> details) {
        return new ErrorResponse(errorCode.getCode(), message, errorCode.getStatus(), Instant.now(), details);
    }
}
