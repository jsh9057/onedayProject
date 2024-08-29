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
@NoArgsConstructor
@AllArgsConstructor
@DynamicInsert
@DynamicUpdate
@SQLRestriction("is_deleted = 'N'")
public class Habit extends BaseEntity{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false, updatable = false)
    private Long habitId;

    @ManyToOne(optional = false) @JoinColumn(name = "userAccountId", columnDefinition = "bigint COMMENT '회원 id'")
    private UserAccount userAccount;

    @Column(nullable = false, columnDefinition = "varchar(100) COMMENT '주제 제목'")
    private String title;

    @Column(nullable = false, columnDefinition = "varchar(10000) COMMENT '주제 내용'")
    private String content;
}
