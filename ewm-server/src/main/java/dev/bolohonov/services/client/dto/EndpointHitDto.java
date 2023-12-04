package dev.bolohonov.services.client.dto;

import lombok.*;

@Getter
@Setter
@ToString
@Builder
@AllArgsConstructor
public class EndpointHitDto {
    /**
     * Идентификатор сервиса для которого записывается информация
     */
    String app;
    /**
     * URI для которого был осуществлен запрос
     */
    String uri;
    /**
     * IP-адрес пользователя, осуществившего запрос
     */
    String ip;
    /**
     * Дата и время, когда был совершен запрос к эндпоинту (в формате "yyyy-MM-dd HH:mm:ss")
     */
    String timestamp;
}
