package tv.mopl.common.exception;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Map;
import org.junit.jupiter.api.Test;

class BusinessExceptionTest {

    @Test
    void createsWithErrorCode() {
        BusinessException exception = new NotFoundException(CommonErrorCode.RESOURCE_NOT_FOUND);

        assertThat(exception.getErrorCode()).isEqualTo(CommonErrorCode.RESOURCE_NOT_FOUND);
        assertThat(exception.getMessage()).isEqualTo(CommonErrorCode.RESOURCE_NOT_FOUND.getMessage());
        assertThat(exception.getDetails()).isEmpty();
    }

    @Test
    void createsWithErrorCodeAndCustomMessage() {
        BusinessException exception = new NotFoundException(CommonErrorCode.RESOURCE_NOT_FOUND, "User not found");

        assertThat(exception.getErrorCode()).isEqualTo(CommonErrorCode.RESOURCE_NOT_FOUND);
        assertThat(exception.getMessage()).isEqualTo("User not found");
        assertThat(exception.getDetails()).isEmpty();
    }

    @Test
    void createsWithErrorCodeAndCause() {
        RuntimeException cause = new RuntimeException("root cause");
        BusinessException exception = new NotFoundException(CommonErrorCode.RESOURCE_NOT_FOUND, cause);

        assertThat(exception.getErrorCode()).isEqualTo(CommonErrorCode.RESOURCE_NOT_FOUND);
        assertThat(exception.getMessage()).isEqualTo(CommonErrorCode.RESOURCE_NOT_FOUND.getMessage());
        assertThat(exception.getCause()).isEqualTo(cause);
        assertThat(exception.getDetails()).isEmpty();
    }

    @Test
    void createsWithErrorCodeAndMessageAndCause() {
        RuntimeException cause = new RuntimeException("root cause");
        BusinessException exception = new NotFoundException(
            CommonErrorCode.RESOURCE_NOT_FOUND, "User not found",
            cause
        );

        assertThat(exception.getErrorCode()).isEqualTo(CommonErrorCode.RESOURCE_NOT_FOUND);
        assertThat(exception.getMessage()).isEqualTo("User not found");
        assertThat(exception.getCause()).isEqualTo(cause);
        assertThat(exception.getDetails()).isEmpty();
    }

    @Test
    void createsWithErrorCodeAndDetails() {
        Map<String, Object> details = Map.of("resourceId", 42);
        BusinessException exception = new NotFoundException(CommonErrorCode.RESOURCE_NOT_FOUND, details);

        assertThat(exception.getErrorCode()).isEqualTo(CommonErrorCode.RESOURCE_NOT_FOUND);
        assertThat(exception.getMessage()).isEqualTo(CommonErrorCode.RESOURCE_NOT_FOUND.getMessage());
        assertThat(exception.getDetails()).containsEntry("resourceId", 42);
    }

    @Test
    void createsWithErrorCodeAndMessageAndDetails() {
        Map<String, Object> details = Map.of("userId", 1);
        BusinessException exception = new NotFoundException(
            CommonErrorCode.RESOURCE_NOT_FOUND,
            "Custom message",
            details
        );

        assertThat(exception.getErrorCode()).isEqualTo(CommonErrorCode.RESOURCE_NOT_FOUND);
        assertThat(exception.getMessage()).isEqualTo("Custom message");
        assertThat(exception.getDetails()).containsEntry("userId", 1);
    }

    @Test
    void notFoundExceptionIsBusinessException() {
        NotFoundException exception = new NotFoundException(CommonErrorCode.RESOURCE_NOT_FOUND);

        assertThat(exception).isInstanceOf(BusinessException.class);
        assertThat(exception.getErrorCode()).isEqualTo(CommonErrorCode.RESOURCE_NOT_FOUND);
    }

    @Test
    void duplicateExceptionIsBusinessException() {
        DuplicateException exception = new DuplicateException(CommonErrorCode.DUPLICATE_RESOURCE, "Already exists");

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
