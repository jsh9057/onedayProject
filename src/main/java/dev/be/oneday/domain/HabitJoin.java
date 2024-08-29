package dev.be.oneday.domain;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.annotations.SQLRestriction;

@Entity
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@DynamicInsert
@DynamicUpdate
@SQLRestriction("is_deleted = 'N'")
public class HabitJoin extends BaseEntity{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false, updatable = false)
    private Long habitJoinId;

    @ManyToOne(optional = false)@JoinColumn(name = "userAccountId", columnDefinition = "bigint COMMENT '회원 id'")
    private UserAccount userAccount;
    @ManyToOne(optional = false)@JoinColumn(name = "habitId", columnDefinition = "bigint COMMENT '습관 id'")
    private Habit habit;
}
