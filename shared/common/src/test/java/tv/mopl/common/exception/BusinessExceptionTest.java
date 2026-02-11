package tv.mopl.common.exception;

import org.junit.jupiter.api.Test;

import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

class BusinessExceptionTest {

    @Test
    void createsWithErrorCode() {
        BusinessException exception = new BusinessException(CommonErrorCode.INTERNAL_SERVER_ERROR);

        assertThat(exception.getErrorCode()).isEqualTo(CommonErrorCode.INTERNAL_SERVER_ERROR);
        assertThat(exception.getMessage()).isEqualTo(CommonErrorCode.INTERNAL_SERVER_ERROR.getMessage());
        assertThat(exception.getDetails()).isEmpty();
    }

    @Test
    void createsWithErrorCodeAndCustomMessage() {
        String customMessage = "Something went wrong";
        BusinessException exception = new BusinessException(CommonErrorCode.INTERNAL_SERVER_ERROR, customMessage);

        assertThat(exception.getErrorCode()).isEqualTo(CommonErrorCode.INTERNAL_SERVER_ERROR);
        assertThat(exception.getMessage()).isEqualTo(customMessage);
        assertThat(exception.getDetails()).isEmpty();
    }

    @Test
    void createsWithErrorCodeAndDetails() {
        Map<String, Object> details = Map.of("resourceId", 42);
        BusinessException exception = new BusinessException(CommonErrorCode.INTERNAL_SERVER_ERROR, details);

        assertThat(exception.getErrorCode()).isEqualTo(CommonErrorCode.INTERNAL_SERVER_ERROR);
        assertThat(exception.getMessage()).isEqualTo(CommonErrorCode.INTERNAL_SERVER_ERROR.getMessage());
        assertThat(exception.getDetails()).containsEntry("resourceId", 42);
    }

    @Test
    void createsWithErrorCodeAndMessageAndDetails() {
        Map<String, Object> details = Map.of("userId", 1);
        BusinessException exception = new BusinessException(
            CommonErrorCode.INTERNAL_SERVER_ERROR,
            "Custom message",
            details);

        assertThat(exception.getErrorCode()).isEqualTo(CommonErrorCode.INTERNAL_SERVER_ERROR);
        assertThat(exception.getMessage()).isEqualTo("Custom message");
        assertThat(exception.getDetails()).containsEntry("userId", 1);
    }

    @Test
    void notFoundExceptionIsBusinessException() {
        NotFoundException exception = new NotFoundException(CommonErrorCode.INTERNAL_SERVER_ERROR);

        assertThat(exception).isInstanceOf(BusinessException.class);
        assertThat(exception.getErrorCode()).isEqualTo(CommonErrorCode.INTERNAL_SERVER_ERROR);
    }

    @Test
    void duplicateExceptionIsBusinessException() {
        DuplicateException exception = new DuplicateException(CommonErrorCode.INVALID_INPUT_VALUE, "Already exists");

        assertThat(exception).isInstanceOf(BusinessException.class);
        assertThat(exception.getMessage()).isEqualTo("Already exists");
    }

    @Test
    void invalidValueExceptionIsBusinessException() {
        InvalidValueException exception = new InvalidValueException(CommonErrorCode.INVALID_INPUT_VALUE);

        assertThat(exception).isInstanceOf(BusinessException.class);
    }

    @Test
    void unauthorizedExceptionIsBusinessException() {
        UnauthorizedException exception = new UnauthorizedException(CommonErrorCode.UNAUTHORIZED);

        assertThat(exception).isInstanceOf(BusinessException.class);
    }

    @Test
    void forbiddenExceptionIsBusinessException() {
        ForbiddenException exception = new ForbiddenException(CommonErrorCode.FORBIDDEN);

        assertThat(exception).isInstanceOf(BusinessException.class);
    }
}
