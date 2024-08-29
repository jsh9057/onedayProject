package dev.be.oneday.domain;

import jakarta.persistence.Column;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.MappedSuperclass;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.io.Serializable;
import java.time.LocalDateTime;

@Getter
@Setter
@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
@DynamicUpdate
@DynamicInsert
public abstract class BaseEntity implements Serializable {

    @CreatedDate
    @Column(updatable = false, columnDefinition = "datetime default CURRENT_TIMESTAMP NOT NULL COMMENT '생성일자'")
    private LocalDateTime createdAt;

    @CreatedBy
    @Column(updatable = false, columnDefinition = "varchar(100) COMMENT '생성자'")
    private String createdBy;

    @LastModifiedDate
    @Column(columnDefinition = "datetime default CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP NOT NULL COMMENT '수정일자'")
    private LocalDateTime modifiedAt;

    @LastModifiedBy
    @Column(columnDefinition = "varchar(100) COMMENT '수정자'")
    private String modifiedBy;

    @Column(columnDefinition = "varchar(1) default 'N' NOT NULL COMMENT '이용가능여부'")
//    @Column(nullable = false)
    private Boolean isDeleted=false;
}
