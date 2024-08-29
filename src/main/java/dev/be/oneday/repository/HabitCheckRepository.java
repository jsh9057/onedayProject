package dev.be.oneday.repository;

import dev.be.oneday.domain.HabitCheck;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface HabitCheckRepository extends JpaRepository<HabitCheck,Long> {
}
