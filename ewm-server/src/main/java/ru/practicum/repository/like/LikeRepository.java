package ru.practicum.repository.like;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.model.like.Like;

public interface LikeRepository extends JpaRepository<Like, Long>, LikeRepositoryCustom {
    Like findByUserIdAndEventId(Long userId, Long eventId);
}
