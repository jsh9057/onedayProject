package dev.be.oneday.domain;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.annotations.SQLRestriction;

import java.util.List;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@DynamicInsert
@DynamicUpdate
@SQLRestriction("is_deleted = 'N'")
public class Keyword extends BaseEntity{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false, updatable = false)
    private Long keywordId;

    @Column(nullable = false, columnDefinition = "varchar(100) COMMENT '제목 키워드'")
    private String keyword;

    @Column(nullable = false, columnDefinition = "bigint COMMENT '습관 id'")
    private Long habitId;
}