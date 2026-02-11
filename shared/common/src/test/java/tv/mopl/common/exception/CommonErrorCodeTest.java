package tv.mopl.common.exception;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Arrays;
import java.util.Set;
import java.util.stream.Collectors;
import org.junit.jupiter.api.Test;

class CommonErrorCodeTest {

    @Test
    void allCodesAreUnique() {
        Set<String> codes = Arrays.stream(CommonErrorCode.values())
            .map(CommonErrorCode::getCode)
            .collect(Collectors.toSet());

        assertThat(codes).hasSameSizeAs(CommonErrorCode.values());
    }

    @Test
    void allStatusCodesAreInValidRange() {
        for (CommonErrorCode errorCode : CommonErrorCode.values()) {
            assertThat(errorCode.getStatus())
                .as("CommonErrorCode.%s status should be between 400 and 599", errorCode.name())
                .isBetween(400, 599);
        }
    }

    @Test
    void allMessagesAreNotBlank() {
        for (CommonErrorCode errorCode : CommonErrorCode.values()) {
            assertThat(errorCode.getMessage())
                .as("CommonErrorCode.%s message should not be blank", errorCode.name())
                .isNotBlank();
        }
    }

    @Test
    void implementsErrorCodeInterface() {
        for (CommonErrorCode errorCode : CommonErrorCode.values()) {
            assertThat(errorCode).isInstanceOf(ErrorCode.class);
        }
    }
}
