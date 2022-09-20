package ru.practicum.model.like;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Table(name = "likes", schema = "public")
@IdClass(LikeId.class)
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Like {
    @Id
    @Column(name = "user_id")
    private Long userId;
    @Id
    @Column(name = "event_id")
    private Long eventId;
    @Column(name = "reason")
    private Boolean reason;
}
