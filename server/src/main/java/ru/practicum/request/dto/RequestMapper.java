package ru.practicum.request.dto;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.practicum.event.Event;
import ru.practicum.event.dto.EventShortDto;
import ru.practicum.request.Request;

import java.util.ArrayList;
import java.util.Collection;

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
        Collection<RequestDto> dtos = new ArrayList<>();
        for (Request request : requests) {
            dtos.add(toRequestDto(request));
        }
        return dtos;
    }
}
