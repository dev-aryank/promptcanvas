package dev.aryank.promptcanvas.repository;

import dev.aryank.promptcanvas.entity.Plan;
import io.micrometer.observation.ObservationFilter;
import jdk.jfr.Registered;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

@Registered
public interface PlanRepository extends JpaRepository<Plan, Long> {
    Optional<Plan> findByStripePriceId(String id);
}
