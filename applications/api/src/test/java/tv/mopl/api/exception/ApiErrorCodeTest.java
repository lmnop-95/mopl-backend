package tv.mopl.api.exception;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Arrays;
import java.util.Set;
import java.util.stream.Collectors;
import org.junit.jupiter.api.Test;
import tv.mopl.common.exception.ErrorCode;

class ApiErrorCodeTest {

    @Test
    void allCodesAreUnique() {
        Set<String> codes = Arrays.stream(ApiErrorCode.values())
            .map(ApiErrorCode::getCode)
            .collect(Collectors.toSet());

        assertThat(codes).hasSameSizeAs(ApiErrorCode.values());
    }

    @Test
    void allStatusCodesAreInValidRange() {
        for (ApiErrorCode errorCode : ApiErrorCode.values()) {
            assertThat(errorCode.getStatus())
                .as("ApiErrorCode.%s status should be between 400 and 599", errorCode.name())
                .isBetween(400, 599);
        }
    }

    @Test
    void allMessagesAreNotBlank() {
        for (ApiErrorCode errorCode : ApiErrorCode.values()) {
            assertThat(errorCode.getMessage())
                .as("ApiErrorCode.%s message should not be blank", errorCode.name())
                .isNotBlank();
        }
    }

    @Test
    void implementsErrorCodeInterface() {
        for (ApiErrorCode errorCode : ApiErrorCode.values()) {
            assertThat(errorCode).isInstanceOf(ErrorCode.class);
        }
    }
}
