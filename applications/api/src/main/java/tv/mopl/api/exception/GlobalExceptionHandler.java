package tv.mopl.api.exception;

import jakarta.validation.ConstraintViolationException;
import java.util.List;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.HttpMediaTypeNotAcceptableException;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingRequestCookieException;
import org.springframework.web.bind.MissingRequestHeaderException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.HandlerMethodValidationException;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.multipart.support.MissingServletRequestPartException;
import org.springframework.web.servlet.resource.NoResourceFoundException;
import tv.mopl.common.exception.BusinessException;
import tv.mopl.common.exception.ErrorCode;
import tv.mopl.common.exception.ErrorResponse;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    // 라우팅

    @ExceptionHandler(NoResourceFoundException.class)
    protected ResponseEntity<ErrorResponse> handleNoResourceFound(NoResourceFoundException e) {
        log.warn("NoResourceFoundException: {}", e.getMessage());
        ErrorResponse response = ErrorResponse.of(
            ApiErrorCode.RESOURCE_NOT_FOUND,
            Map.of("path", e.getResourcePath())
        );
        return ResponseEntity.status(ApiErrorCode.RESOURCE_NOT_FOUND.getStatus()).body(response);
    }

    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    protected ResponseEntity<ErrorResponse> handleHttpRequestMethodNotSupported(
        HttpRequestMethodNotSupportedException e
    ) {
        log.warn("HttpRequestMethodNotSupportedException: {}", e.getMessage());
        String[] supported = e.getSupportedMethods();
        ErrorResponse response = ErrorResponse.of(
            ApiErrorCode.METHOD_NOT_ALLOWED, Map.of(
                "method", e.getMethod(),
                "supportedMethods", supported == null ? List.of() : List.of(supported)
            )
        );
        return ResponseEntity.status(ApiErrorCode.METHOD_NOT_ALLOWED.getStatus()).body(response);
    }

    @ExceptionHandler(HttpMediaTypeNotAcceptableException.class)
    protected ResponseEntity<ErrorResponse> handleHttpMediaTypeNotAcceptable(HttpMediaTypeNotAcceptableException e) {
        log.warn("HttpMediaTypeNotAcceptableException: {}", e.getMessage());
        List<String> supported = e.getSupportedMediaTypes().stream()
            .map(Object::toString)
            .toList();
        ErrorResponse response = ErrorResponse.of(
            ApiErrorCode.MEDIA_TYPE_NOT_ACCEPTABLE,
            Map.of("supportedTypes", supported)
        );
        return ResponseEntity.status(ApiErrorCode.MEDIA_TYPE_NOT_ACCEPTABLE.getStatus()).body(response);
    }

    @ExceptionHandler(HttpMediaTypeNotSupportedException.class)
    protected ResponseEntity<ErrorResponse> handleHttpMediaTypeNotSupported(HttpMediaTypeNotSupportedException e) {
        log.warn("HttpMediaTypeNotSupportedException: {}", e.getMessage());
        List<String> supported = e.getSupportedMediaTypes().stream()
            .map(Object::toString)
            .toList();
        ErrorResponse response = ErrorResponse.of(
            ApiErrorCode.MEDIA_TYPE_NOT_SUPPORTED, Map.of(
                "contentType", e.getContentType() == null ? "unknown" : e.getContentType().toString(),
                "supportedTypes", supported
            )
        );
        return ResponseEntity.status(ApiErrorCode.MEDIA_TYPE_NOT_SUPPORTED.getStatus()).body(response);
    }

    // 바인딩

    @ExceptionHandler(MissingServletRequestParameterException.class)
    protected ResponseEntity<ErrorResponse> handleMissingServletRequestParameter(
        MissingServletRequestParameterException e
    ) {
        log.warn("MissingServletRequestParameterException: {}", e.getMessage());
        ErrorResponse response = ErrorResponse.of(
            ApiErrorCode.MISSING_PARAMETER,
            Map.of("parameter", e.getParameterName(), "expectedType", e.getParameterType())
        );
        return ResponseEntity.status(ApiErrorCode.MISSING_PARAMETER.getStatus()).body(response);
    }

    @ExceptionHandler(MissingServletRequestPartException.class)
    protected ResponseEntity<ErrorResponse> handleMissingServletRequestPart(MissingServletRequestPartException e) {
        log.warn("MissingServletRequestPartException: {}", e.getMessage());
        ErrorResponse response = ErrorResponse.of(
            ApiErrorCode.MISSING_PART,
            Map.of("partName", e.getRequestPartName())
        );
        return ResponseEntity.status(ApiErrorCode.MISSING_PART.getStatus()).body(response);
    }

    @ExceptionHandler(MissingRequestCookieException.class)
    protected ResponseEntity<ErrorResponse> handleMissingRequestCookie(MissingRequestCookieException e) {
        log.warn("MissingRequestCookieException: {}", e.getMessage());
        ErrorResponse response = ErrorResponse.of(
            ApiErrorCode.MISSING_COOKIE,
            Map.of("cookieName", e.getCookieName())
        );
        return ResponseEntity.status(ApiErrorCode.MISSING_COOKIE.getStatus()).body(response);
    }

    @ExceptionHandler(MissingRequestHeaderException.class)
    protected ResponseEntity<ErrorResponse> handleMissingRequestHeader(MissingRequestHeaderException e) {
        log.warn("MissingRequestHeaderException: {}", e.getMessage());
        ErrorResponse response = ErrorResponse.of(
            ApiErrorCode.MISSING_HEADER,
            Map.of("headerName", e.getHeaderName())
        );
        return ResponseEntity.status(ApiErrorCode.MISSING_HEADER.getStatus()).body(response);
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    protected ResponseEntity<ErrorResponse> handleMethodArgumentTypeMismatch(MethodArgumentTypeMismatchException e) {
        log.warn("MethodArgumentTypeMismatchException: {}", e.getMessage());
        Class<?> requiredType = e.getRequiredType();
        ErrorResponse response = ErrorResponse.of(
            ApiErrorCode.TYPE_MISMATCH, Map.of(
                "parameter", e.getName(),
                "rejectedValue", e.getValue() == null ? "unknown" : e.getValue().toString(),
                "expectedType", requiredType == null ? "unknown" : requiredType.getSimpleName()
            )
        );
        return ResponseEntity.status(ApiErrorCode.TYPE_MISMATCH.getStatus()).body(response);
    }

    // 파싱

    @ExceptionHandler(HttpMessageNotReadableException.class)
    protected ResponseEntity<ErrorResponse> handleHttpMessageNotReadable(HttpMessageNotReadableException e) {
        log.warn("HttpMessageNotReadableException: {}", e.getMessage());
        ErrorResponse response = ErrorResponse.of(ApiErrorCode.MESSAGE_NOT_READABLE);
        return ResponseEntity.status(ApiErrorCode.MESSAGE_NOT_READABLE.getStatus()).body(response);
    }

    // 검증

    @ExceptionHandler(MethodArgumentNotValidException.class)
    protected ResponseEntity<ErrorResponse> handleMethodArgumentNotValid(MethodArgumentNotValidException e) {
        log.warn("MethodArgumentNotValidException: {}", e.getMessage());
        List<String> fields = e.getBindingResult().getFieldErrors().stream()
            .map(error -> error.getField() + ": " + error.getDefaultMessage())
            .toList();
        ErrorResponse response = ErrorResponse.of(ApiErrorCode.VALIDATION_FAILED, Map.of("fields", fields));
        return ResponseEntity.status(ApiErrorCode.VALIDATION_FAILED.getStatus()).body(response);
    }

    @ExceptionHandler(HandlerMethodValidationException.class)
    protected ResponseEntity<ErrorResponse> handleHandlerMethodValidation(HandlerMethodValidationException e) {
        log.warn("HandlerMethodValidationException: {}", e.getMessage());
        List<String> violations = e.getParameterValidationResults().stream()
            .flatMap(result -> result.getResolvableErrors().stream()
                .map(error -> error.getDefaultMessage() != null ? error.getDefaultMessage() : "unknown"))
            .toList();
        ErrorResponse response = ErrorResponse.of(ApiErrorCode.VALIDATION_FAILED, Map.of("fields", violations));
        return ResponseEntity.status(ApiErrorCode.VALIDATION_FAILED.getStatus()).body(response);
    }

    @ExceptionHandler(ConstraintViolationException.class)
    protected ResponseEntity<ErrorResponse> handleConstraintViolation(ConstraintViolationException e) {
        log.warn("ConstraintViolationException: {}", e.getMessage());
        List<String> violations = e.getConstraintViolations().stream()
            .map(v -> v.getPropertyPath() + ": " + v.getMessage())
            .toList();
        ErrorResponse response = ErrorResponse.of(ApiErrorCode.CONSTRAINT_VIOLATION, Map.of("violations", violations));
        return ResponseEntity.status(ApiErrorCode.CONSTRAINT_VIOLATION.getStatus()).body(response);
    }

    // 비즈니스

    @ExceptionHandler(BusinessException.class)
    protected ResponseEntity<ErrorResponse> handleBusinessException(BusinessException e) {
        log.warn("BusinessException: {}", e.getMessage());
        ErrorCode errorCode = e.getErrorCode();
        ErrorResponse response = ErrorResponse.of(errorCode, e.getMessage(), e.getDetails());
        return ResponseEntity.status(errorCode.getStatus()).body(response);
    }

    // 서버

    @ExceptionHandler(Exception.class)
    protected ResponseEntity<ErrorResponse> handleException(Exception e) {
        log.error("Unexpected exception", e);
        ErrorResponse response = ErrorResponse.of(ApiErrorCode.INTERNAL_SERVER_ERROR);
        return ResponseEntity.status(ApiErrorCode.INTERNAL_SERVER_ERROR.getStatus()).body(response);
    }
}
