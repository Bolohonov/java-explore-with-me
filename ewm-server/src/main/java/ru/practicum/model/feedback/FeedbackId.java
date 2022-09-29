package ru.practicum.model.feedback;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class FeedbackId implements Serializable {
    private Long userId;
    private Long eventId;
}
