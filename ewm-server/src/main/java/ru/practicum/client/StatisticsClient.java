package ru.practicum.client;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.util.DefaultUriBuilderFactory;
import ru.practicum.client.dto.EndpointHitDto;

@Service
public class StatisticsClient extends BaseClient {
    private static final String API_PREFIX = "/hit";

    @Autowired
    public StatisticsClient(@Value("${statistics.url}") String serverUrl, RestTemplateBuilder builder) {
        super(
                builder
                        .uriTemplateHandler(new DefaultUriBuilderFactory(serverUrl + API_PREFIX))
                        .requestFactory(HttpComponentsClientHttpRequestFactory::new)
                        .build()
        );
    }

    public ResponseEntity<Object> addEndpointHit(String userIp, String uri) {
        return post("", EndpointHitDto.builder()
                .app("ExploreWithMe")
                .uri(uri)
                .ip(userIp)
                .timestamp(System.currentTimeMillis() / 1000L)
                .build());
    }
}
