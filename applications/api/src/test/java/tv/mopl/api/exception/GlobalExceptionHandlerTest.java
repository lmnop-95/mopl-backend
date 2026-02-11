package tv.mopl.api.exception;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import jakarta.validation.ConstraintViolationException;
import jakarta.validation.Valid;
import jakarta.validation.Validation;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import java.util.List;
import java.util.Map;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.HttpMediaTypeNotAcceptableException;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import tv.mopl.common.exception.CommonErrorCode;
import tv.mopl.common.exception.NotFoundException;

@WebMvcTest
@Import(GlobalExceptionHandlerTest.TestController.class)
class GlobalExceptionHandlerTest {

    @Autowired
    private MockMvc mockMvc;

    // 라우팅

    @Test
    void handleNoResourceFound() throws Exception {
        mockMvc.perform(get("/nonexistent"))
            .andExpect(status().isNotFound())
            .andExpect(jsonPath("$.code").value("API001"));
    }

    @Test
    void handleHttpRequestMethodNotSupported() throws Exception {
        mockMvc.perform(post("/test/type-mismatch").param("number", "1"))
            .andExpect(status().isMethodNotAllowed())
            .andExpect(jsonPath("$.code").value("API002"))
            .andExpect(jsonPath("$.details.method").value("POST"))
            .andExpect(jsonPath("$.details.supportedMethods").isArray());
    }

    @Test
    void handleHttpMediaTypeNotAcceptable() throws Exception {
        mockMvc.perform(get("/test/not-acceptable"))
            .andExpect(status().isNotAcceptable())
            .andExpect(jsonPath("$.code").value("API003"))
            .andExpect(jsonPath("$.details.supportedTypes").isArray());
    }

    @Test
    void handleHttpMediaTypeNotSupported() throws Exception {
        mockMvc.perform(post("/test/validation")
            .contentType(MediaType.APPLICATION_XML)
            .content("<name>test</name>"))
            .andExpect(status().isUnsupportedMediaType())
            .andExpect(jsonPath("$.code").value("API004"))
            .andExpect(jsonPath("$.details.supportedTypes").isArray());
    }

    // 바인딩

    @Test
    void handleMissingServletRequestParameter() throws Exception {
        mockMvc.perform(get("/test/type-mismatch"))
            .andExpect(status().isBadRequest())
            .andExpect(jsonPath("$.code").value("API005"))
            .andExpect(jsonPath("$.details.parameter").value("number"));
    }

    @Test
    void handleMissingServletRequestPart() throws Exception {
        mockMvc.perform(multipart("/test/multipart"))
            .andExpect(status().isBadRequest())
            .andExpect(jsonPath("$.code").value("API006"))
            .andExpect(jsonPath("$.details.partName").value("file"));
    }

    @Test
    void handleMissingRequestCookie() throws Exception {
        mockMvc.perform(get("/test/cookie"))
            .andExpect(status().isBadRequest())
            .andExpect(jsonPath("$.code").value("API007"))
            .andExpect(jsonPath("$.details.cookieName").value("SESSION"));
    }

    @Test
    void handleMissingRequestHeader() throws Exception {
        mockMvc.perform(get("/test/header"))
            .andExpect(status().isBadRequest())
            .andExpect(jsonPath("$.code").value("API008"))
            .andExpect(jsonPath("$.details.headerName").value("X-Custom"));
    }

    @Test
    void handleMethodArgumentTypeMismatch() throws Exception {
        mockMvc.perform(get("/test/type-mismatch").param("number", "abc"))
            .andExpect(status().isBadRequest())
            .andExpect(jsonPath("$.code").value("API009"))
            .andExpect(jsonPath("$.details.parameter").value("number"))
            .andExpect(jsonPath("$.details.rejectedValue").value("abc"))
            .andExpect(jsonPath("$.details.expectedType").value("int"));
    }

    // 파싱

    @Test
    void handleHttpMessageNotReadable() throws Exception {
        mockMvc.perform(post("/test/validation")
            .contentType(MediaType.APPLICATION_JSON)
            .content("invalid json"))
            .andExpect(status().isBadRequest())
            .andExpect(jsonPath("$.code").value("API010"));
    }

    // 검증

    @Test
    void handleMethodArgumentNotValid() throws Exception {
        mockMvc.perform(post("/test/validation")
            .contentType(MediaType.APPLICATION_JSON)
            .content("{\"name\": \"\"}"))
            .andExpect(status().isBadRequest())
            .andExpect(jsonPath("$.code").value("API011"))
            .andExpect(jsonPath("$.details.fields").isArray())
            .andExpect(jsonPath("$.details.fields[0]").isString());
    }

    @Test
    void handleHandlerMethodValidation() throws Exception {
        mockMvc.perform(get("/test/validated-param").param("number", "0"))
            .andExpect(status().isBadRequest())
            .andExpect(jsonPath("$.code").value("API011"))
            .andExpect(jsonPath("$.details.fields").isArray());
    }

    @Test
    void handleConstraintViolation() throws Exception {
        mockMvc.perform(get("/test/constraint-violation"))
            .andExpect(status().isBadRequest())
            .andExpect(jsonPath("$.code").value("API012"))
            .andExpect(jsonPath("$.details.violations").isArray());
    }

    // 비즈니스

    @Test
    void handleBusinessException() throws Exception {
        mockMvc.perform(get("/test/business-exception"))
            .andExpect(status().isNotFound())
            .andExpect(jsonPath("$.code").value("C003"))
            .andExpect(jsonPath("$.message").value("Resource not found"));
    }

    @Test
    void handleBusinessExceptionWithDetails() throws Exception {
        mockMvc.perform(get("/test/business-exception-with-details"))
            .andExpect(status().isNotFound())
            .andExpect(jsonPath("$.code").value("C003"))
            .andExpect(jsonPath("$.details.resourceId").value(42));
    }

    // 서버

    @Test
    void handleException() throws Exception {
        mockMvc.perform(get("/test/unexpected-exception"))
            .andExpect(status().isInternalServerError())
            .andExpect(jsonPath("$.code").value("API013"));
    }

    @RestController
    static class TestController {

        @PostMapping("/test/validation")
        void validation(@Valid @RequestBody TestRequest request) {
        }

        @GetMapping("/test/type-mismatch")
        void typeMismatch(@RequestParam int number) {
        }

        @GetMapping("/test/validated-param")
        void validatedParam(@RequestParam @Min(1) int number) {
        }

        @PostMapping("/test/multipart")
        void multipart(@RequestPart("file") MultipartFile file) {
        }

        @GetMapping("/test/cookie")
        void cookie(@CookieValue("SESSION") String session) {
        }

        @GetMapping("/test/header")
        void header(@RequestHeader("X-Custom") String custom) {
        }

        @GetMapping("/test/not-acceptable")
        void notAcceptable() throws HttpMediaTypeNotAcceptableException {
            throw new HttpMediaTypeNotAcceptableException(List.of(MediaType.APPLICATION_JSON));
        }

        @GetMapping("/test/constraint-violation")
        void constraintViolation() {
            throw new ConstraintViolationException(
                Validation.buildDefaultValidatorFactory()
                    .getValidator()
                    .validate(new TestRequest(""))
            );
        }

        @GetMapping("/test/business-exception")
        void businessException() {
            throw new NotFoundException(CommonErrorCode.RESOURCE_NOT_FOUND, "Resource not found");
        }

        @GetMapping("/test/business-exception-with-details")
        void businessExceptionWithDetails() {
            throw new NotFoundException(CommonErrorCode.RESOURCE_NOT_FOUND, Map.of("resourceId", 42));
        }

        @GetMapping("/test/unexpected-exception")
        void unexpectedException() {
            throw new RuntimeException("Unexpected");
        }
    }

    record TestRequest(@NotBlank String name) {
    }
}
