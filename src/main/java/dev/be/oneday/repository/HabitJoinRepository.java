package dev.be.oneday.repository;

import dev.be.oneday.domain.HabitJoin;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface HabitJoinRepository extends JpaRepository<HabitJoin,Long> {
}
