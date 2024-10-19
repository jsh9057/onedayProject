package dev.be.oneday.repository;

import dev.be.oneday.domain.Habit;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface HabitRepository extends JpaRepository<Habit,Long> {
    @Query("select h from Habit h where h.habitId in :habitIds order by h.habitId DESC")
    Page<Habit> findHabitIds(List<Long> habitIds, Pageable pageable);
}
