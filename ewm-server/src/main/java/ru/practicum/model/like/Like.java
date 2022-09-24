package ru.practicum.model.like;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Table(name = "likes", schema = "public")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Like {
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
    @Column(name = "reason")
    private Boolean reason;

    public Like(Long userId, Long eventId, Boolean reason) {
        this.userId = userId;
        this.eventId = eventId;
        this.reason = reason;
    }
}
