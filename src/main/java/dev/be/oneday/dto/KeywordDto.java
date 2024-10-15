package dev.be.oneday.dto;

import dev.be.oneday.domain.Keyword;
import dev.be.oneday.exception.BaseException;
import dev.be.oneday.exception.ErrorType;
import jakarta.persistence.Column;
import lombok.*;

import java.io.Serializable;
import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class KeywordDto implements Serializable {
    private Long keywordId;

    private String keyword;

    private Long habitId;

    private LocalDateTime createdAt;

    private String createdBy;

    private LocalDateTime modifiedAt;

    private String modifiedBy;

    private Boolean isDeleted;

    public static KeywordDto from(Keyword keyword){
        if(keyword==null){
            throw new BaseException(ErrorType.VALUE_IS_NULL, "keyword is null");
        }

        return KeywordDto.builder()
                .keywordId(keyword.getKeywordId())
                .keyword(keyword.getKeyword())
                .habitId(keyword.getHabitId())
                .createdAt(keyword.getCreatedAt())
                .createdBy(keyword.getCreatedBy())
                .modifiedAt(keyword.getModifiedAt())
                .modifiedBy(keyword.getModifiedBy())
                .isDeleted(keyword.getIsDeleted())
                .build();

    }
}
