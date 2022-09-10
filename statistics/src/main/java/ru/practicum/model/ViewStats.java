package ru.practicum.model;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "view_stats", schema = "public")
@Getter
@Setter
@ToString
public class ViewStats {
}
