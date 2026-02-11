package tv.mopl.common.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum CommonErrorCode implements ErrorCode {

    INVALID_INPUT_VALUE(400, "C001", "잘못된 입력 값입니다"),
    INVALID_TYPE_VALUE(400, "C002", "잘못된 타입 값입니다"),
    RESOURCE_NOT_FOUND(404, "C003", "요청한 리소스를 찾을 수 없습니다"),
    DUPLICATE_RESOURCE(409, "C004", "이미 존재하는 리소스입니다"),
    UNAUTHORIZED(401, "C005", "인증이 필요합니다"),
    FORBIDDEN(403, "C006", "접근이 거부되었습니다");

    private final int status;
    private final String code;
    private final String message;
}
