package ru.practicum.repository.like;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.model.like.Like;
import ru.practicum.model.like.LikeId;

public interface LikeRepository extends JpaRepository<Like, LikeId>, LikeCustomRepository {
    Like findByUserIdAndEventId(Long userId, Long eventId);
}
