package ru.practicum.repository.event;

import ru.practicum.model.event.Event;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.Map;
import java.util.Set;

public interface EventRepositoryCustom {
    /**
     * Получить список событий, содержащий text в полях annotation, description,
     * из выбранных категорий setOfCategories,
     * требующих/или нет/ оплаты paid
     * за заданный временной интервал timeMap
     * доступных для участия onlyAvailable (число одобренных заявок меньше установленного лимита)
     * сортировка по полю sort
     * pagination from & size
     */
    Collection<Event> getEvents(String text,
                                Set<Long> setOfCategories,
                                Boolean paid,
                                Map<String, LocalDateTime> timeMap,
                                Boolean onlyAvailable,
                                String sort,
                                Integer from, Integer size);

    /**
     * Получить список событий администратором
     * определенных пользователей ids,
     * определенных статусов states,
     * из выбранных категорий setOfCategories,
     * pagination from & size
     */
    Collection<Event> getEventsByAdmin(Set<Long> ids,
                                       Set<String> states,
                                       Set<Long> setOfCategories,
                                       Map<String, LocalDateTime> timeMap,
                                       Integer fom, Integer size);
}
