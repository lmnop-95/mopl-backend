package tv.mopl.common.exception;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Map;
import org.junit.jupiter.api.Test;

class ErrorResponseTest {

    @Test
    void createsFromErrorCode() {
        ErrorResponse response = ErrorResponse.of(CommonErrorCode.INVALID_INPUT_VALUE);

        assertThat(response.code()).isEqualTo("C001");
        assertThat(response.message()).isEqualTo("잘못된 입력 값입니다");
        assertThat(response.status()).isEqualTo(400);
        assertThat(response.timestamp()).isNotNull();
        assertThat(response.details()).isEmpty();
    }

    @Test
    void createsFromErrorCodeWithCustomMessage() {
        ErrorResponse response = ErrorResponse.of(CommonErrorCode.RESOURCE_NOT_FOUND, "User not found");

        assertThat(response.code()).isEqualTo("C003");
        assertThat(response.message()).isEqualTo("User not found");
        assertThat(response.status()).isEqualTo(404);
        assertThat(response.timestamp()).isNotNull();
        assertThat(response.details()).isEmpty();
    }

    @Test
    void createsFromErrorCodeWithDetails() {
        Map<String, Object> details = Map.of("parameter", "email", "reason", "invalid format");

        ErrorResponse response = ErrorResponse.of(CommonErrorCode.INVALID_INPUT_VALUE, details);

        assertThat(response.code()).isEqualTo("C001");
        assertThat(response.message()).isEqualTo("잘못된 입력 값입니다");
        assertThat(response.status()).isEqualTo(400);
        assertThat(response.details()).containsEntry("parameter", "email");
        assertThat(response.details()).containsEntry("reason", "invalid format");
    }

    @Test
    void createsFromErrorCodeWithCustomMessageAndDetails() {
        Map<String, Object> details = Map.of("field", "name");

        ErrorResponse response = ErrorResponse.of(CommonErrorCode.INVALID_INPUT_VALUE, "검증 실패", details);

        assertThat(response.code()).isEqualTo("C001");
        assertThat(response.message()).isEqualTo("검증 실패");
        assertThat(response.status()).isEqualTo(400);
        assertThat(response.details()).containsEntry("field", "name");
    }
}
