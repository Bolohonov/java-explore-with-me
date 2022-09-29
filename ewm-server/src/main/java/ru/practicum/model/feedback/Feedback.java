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
@IdClass(FeedbackId.class)
public class Feedback {
    /**
     * уникальный идентификатор пользователя
     */
    @Id
    @Column(name = "user_id")
    private Long userId;
    /**
     * уникальный идентификатор события
     */
    @Id
    @Column(name = "event_id")
    private Long eventId;
    /**
     * флаг like - True, dislike - False
     */
    @Column(name = "is_like")
    private Boolean isLike;
}
