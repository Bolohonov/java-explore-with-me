package dev.bolohonov.errors;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class ApiError extends RuntimeException {
    private HttpStatus status;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy hh:mm:ss")
    private LocalDateTime timestamp;
    private String message;
    private String reason;
    private List<ApiError> subErrors = new ArrayList<>();

    private ApiError() {
        timestamp = LocalDateTime.now();
    }

    public ApiError(HttpStatus status, String message, String reason) {
        this.status = status;
        this.message = message;
        this.reason = reason;
    }

    public ApiError(HttpStatus status, String message, String reason, List<ApiError> subErrors) {
        this.status = status;
        this.message = message;
        this.reason = reason;
        this.subErrors = subErrors;
    }
}
