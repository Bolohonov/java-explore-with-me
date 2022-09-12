package ru.practicum.user.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.user.User;

public interface UserRepository extends JpaRepository<User, Long>, UserRepositoryCustom {
}
