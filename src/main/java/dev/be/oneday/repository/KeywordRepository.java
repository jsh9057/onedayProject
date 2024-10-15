package dev.be.oneday.repository;

import dev.be.oneday.domain.Keyword;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Set;

@Repository
public interface KeywordRepository extends JpaRepository<Keyword, Long> {
    @Query("select k.habitId from Keyword k where k.keyword in :keywords")
    List<Long> findKeywordsIn(Set<String> keywords, Pageable pageable);

    @Modifying
    @Query("delete from Keyword k where k.keyword in :keywords AND k.habitId = :habitId")
    void deleteKeywords(Set<String> keywords, Long habitId);

    void deleteAllByHabitId(Long habitId);
}
