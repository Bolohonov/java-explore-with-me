package ru.practicum.model;

import lombok.*;

@Getter
@Setter
@ToString
@AllArgsConstructor
@Builder
public class ViewStats {
    String app;
    String uri;
    Long hits;
}
