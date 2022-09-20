package ru.practicum.model.like;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
public class LikeId implements Serializable {
    private Long userId;
    private Long eventId;
}
