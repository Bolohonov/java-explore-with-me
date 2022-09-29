package ru.practicum.model.feedback;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Table(name = "feedbacks", schema = "public")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Feedback {
    /**
     * уникальный идентификатор
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    /**
     * уникальный идентификатор пользователя
     */
    @Column(name = "user_id")
    private Long userId;
    /**
     * уникальный идентификатор события
     */
    @Column(name = "event_id")
    private Long eventId;
    /**
     * флаг like - True, dislike - False
     */
    @Column(name = "is_like")
    private Boolean isLike;

    public Feedback(Long userId, Long eventId, Boolean isLike) {
        this.userId = userId;
        this.eventId = eventId;
        this.isLike = isLike;
    }
}
