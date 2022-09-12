package ru.practicum;

import lombok.*;

import java.util.List;

@AllArgsConstructor
@Getter
@Setter
@ToString
@Builder
public class ErrorResponse {
    private String id;
    private List<String> errors;
    private String message;
    private String reason;
    private String status;
    private String timestamp;
}
