package tv.mopl.common.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum CommonErrorCode implements ErrorCode {

    INVALID_INPUT_VALUE(400, "C001", "잘못된 입력 값입니다"),
    INVALID_TYPE_VALUE(400, "C002", "잘못된 타입 값입니다"),
    RESOURCE_NOT_FOUND(404, "C003", "요청한 리소스를 찾을 수 없습니다"),
    METHOD_NOT_ALLOWED(405, "C004", "허용되지 않는 HTTP 메서드입니다"),
    DUPLICATE_RESOURCE(409, "C005", "이미 존재하는 리소스입니다"),
    INTERNAL_SERVER_ERROR(500, "C006", "서버 내부 오류가 발생했습니다"),
    UNAUTHORIZED(401, "A001", "인증이 필요합니다"),
    FORBIDDEN(403, "A002", "접근이 거부되었습니다");

    private final int status;
    private final String code;
    private final String message;
}
