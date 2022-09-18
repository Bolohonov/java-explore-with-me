package ru.practicum.services.event;

import ru.practicum.model.event.dto.EventFullDto;
import ru.practicum.model.event.dto.EventShortDto;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

public interface EventServicePublic {
    /**
     * Получить список событий, содержащий text в полях annotation, description,
     * из выбранных категорий сategories,
     * требующих/или нет/ оплаты paid
     * за заданный временной интервал timeMap
     * доступных для участия onlyAvailable (число одобренных заявок меньше установленного лимита)
     * сортировка по полю sort
     * pagination from & size
     */
    Collection<EventShortDto> getEvents(String text, List<Long> categories, Boolean paid, String rangeStart,
                                        String rangeEnd, Boolean onlyAvailable, String sort,
                                        Integer from, Integer size);

    /**
     * Получить опубликованное событие по id
     */
    Optional<EventFullDto> getPublishedEventById(Long eventId);
}
