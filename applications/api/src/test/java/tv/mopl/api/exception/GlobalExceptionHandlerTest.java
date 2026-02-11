package tv.mopl.api.exception;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.bind.annotation.*;
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

    // 바인딩

    @Test
    void handleMissingServletRequestParameter() throws Exception {
        mockMvc.perform(get("/test/type-mismatch"))
            .andExpect(status().isBadRequest())
            .andExpect(jsonPath("$.code").value("API005"))
            .andExpect(jsonPath("$.details.parameter").value("number"));
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

    // 비즈니스

    @Test
    void handleBusinessException() throws Exception {
        mockMvc.perform(get("/test/business-exception"))
            .andExpect(status().isNotFound())
            .andExpect(jsonPath("$.code").value("C003"))
            .andExpect(jsonPath("$.message").value("Resource not found"));
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

        @GetMapping("/test/cookie")
        void cookie(@CookieValue("SESSION") String session) {
        }

        @GetMapping("/test/header")
        void header(@RequestHeader("X-Custom") String custom) {
        }

        @GetMapping("/test/business-exception")
        void businessException() {
            throw new NotFoundException(CommonErrorCode.RESOURCE_NOT_FOUND, "Resource not found");
        }

        @GetMapping("/test/unexpected-exception")
        void unexpectedException() {
            throw new RuntimeException("Unexpected");
        }
    }

    record TestRequest(@NotBlank String name) {
    }
}
