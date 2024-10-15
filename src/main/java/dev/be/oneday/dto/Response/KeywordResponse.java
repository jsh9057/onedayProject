package dev.be.oneday.dto.Response;

import dev.be.oneday.dto.KeywordDto;
import jakarta.persistence.Column;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
@Builder
public class KeywordResponse implements Serializable {
    Long keywordId;
    String keyword;
    Long habitId;

    static public KeywordResponse from(KeywordDto keywordDto){
        return KeywordResponse.builder()
                .keywordId(keywordDto.getKeywordId())
                .keyword(keywordDto.getKeyword())
                .habitId(keywordDto.getHabitId())
                .build();
    }
}
