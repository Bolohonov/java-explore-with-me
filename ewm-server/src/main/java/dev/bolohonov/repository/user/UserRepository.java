package dev.bolohonov.repository.user;

import org.springframework.data.jpa.repository.JpaRepository;
import dev.bolohonov.model.user.User;

public interface UserRepository extends JpaRepository<User, Long>, UserRepositoryCustom {
}
