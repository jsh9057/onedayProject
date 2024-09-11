package dev.be.oneday.repository;

import dev.be.oneday.domain.HabitJoin;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface HabitJoinRepository extends JpaRepository<HabitJoin,Long> {
    Optional<HabitJoin> findByHabit_HabitIdAndUserAccount_UserAccountId(Long habitId, Long userAccountId);
    Page<HabitJoin> findByUserAccount_UserAccountId(Long userAccountId, Pageable pageable);
    Page<HabitJoin> findByHabit_HabitId(Long habitId, Pageable pageable);
}
