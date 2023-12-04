package dev.bolohonov.mappers.request;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import dev.bolohonov.model.request.Request;
import dev.bolohonov.model.request.dto.RequestDto;

import java.util.Collection;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class RequestMapper {
    public static RequestDto toRequestDto(Request request) {
        return new RequestDto(
                request.getId(),
                request.getCreated(),
                request.getEvent(),
                request.getRequester(),
                request.getStatus()
        );
    }

    public static Collection<RequestDto> toRequestDto(Collection<Request> requests) {
        return requests.stream()
                .map(RequestMapper::toRequestDto)
                .collect(Collectors.toList());
    }
}
