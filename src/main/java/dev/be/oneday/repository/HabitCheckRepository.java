package dev.be.oneday.repository;

import dev.be.oneday.domain.HabitCheck;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;

@Repository
public interface HabitCheckRepository extends JpaRepository<HabitCheck,Long> {
    Page<HabitCheck> findAllByUserAccount_UserAccountIdAndHabit_HabitIdAndCreatedAtBetween(Long userAccountId, Long habitId, LocalDateTime start, LocalDateTime end, Pageable pageable);
}
