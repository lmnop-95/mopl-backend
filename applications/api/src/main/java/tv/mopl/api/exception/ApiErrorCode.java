package tv.mopl.api.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import tv.mopl.common.exception.ErrorCode;

@Getter
@RequiredArgsConstructor
public enum ApiErrorCode implements ErrorCode {

    // 라우팅
    RESOURCE_NOT_FOUND(404, "API001", "요청한 리소스를 찾을 수 없습니다"),
    METHOD_NOT_ALLOWED(405, "API002", "허용되지 않는 HTTP 메서드입니다"),
    MEDIA_TYPE_NOT_ACCEPTABLE(406, "API003", "허용되지 않는 미디어 타입입니다"),
    MEDIA_TYPE_NOT_SUPPORTED(415, "API004", "지원하지 않는 미디어 타입입니다"),

    // 바인딩
    MISSING_PARAMETER(400, "API005", "필수 요청 파라미터가 누락되었습니다"),
    MISSING_PART(400, "API006", "필수 멀티파트 파라미터가 누락되었습니다"),
    MISSING_COOKIE(400, "API007", "필수 쿠키가 누락되었습니다"),
    MISSING_HEADER(400, "API008", "필수 헤더가 누락되었습니다"),
    TYPE_MISMATCH(400, "API009", "잘못된 타입 값입니다"),

    // 파싱
    MESSAGE_NOT_READABLE(400, "API010", "요청 본문을 읽을 수 없습니다"),

    // 검증
    VALIDATION_FAILED(400, "API011", "요청 값 검증에 실패했습니다"),
    CONSTRAINT_VIOLATION(400, "API012", "요청 값이 제약 조건을 위반했습니다"),

    // 서버
    INTERNAL_SERVER_ERROR(500, "API013", "서버 내부 오류가 발생했습니다");

    private final int status;
    private final String code;
    private final String message;
}
