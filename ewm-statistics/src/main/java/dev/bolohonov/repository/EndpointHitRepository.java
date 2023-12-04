package dev.bolohonov.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import dev.bolohonov.model.EndpointHit;

public interface EndpointHitRepository extends JpaRepository<EndpointHit, Long>, EndpointHitRepositoryCustom {
}
