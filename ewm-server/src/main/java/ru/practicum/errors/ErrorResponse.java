package ru.practicum.errors;

import lombok.*;

@AllArgsConstructor
@Getter
@Setter
@ToString
@Builder
public class ErrorResponse {
    private String id;
    private String errors;
    private String message;
    private String reason;
    private String status;
    private String timestamp;

    public ErrorResponse(String status, String message, String reason, String errors) {
        this.status = status;
        this.message = message;
        this.reason = reason;
        this.errors = errors;
    }
}
