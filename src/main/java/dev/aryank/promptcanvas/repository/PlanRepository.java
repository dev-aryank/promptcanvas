package dev.aryank.promptcanvas.repository;

import dev.aryank.promptcanvas.entity.Plan;
import jdk.jfr.Registered;
import org.springframework.data.jpa.repository.JpaRepository;

@Registered
public interface PlanRepository extends JpaRepository<Plan, Long> {
}
