package dev.be.oneday.domain;

import lombok.*;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.MongoId;

import java.util.List;

@Document(collection = "keyword") // 실제 몽고 DB 컬렉션 이름
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class KeywordMongo {
    @MongoId
    private String keyword;

    private List<Long> habitIds;

    public void addHabitId(Long habitId){
        this.habitIds.add(habitId);
    }
    public void addAllHabitId(List<Long> habitIdList){ this.habitIds.addAll(habitIdList); }

    @Override
    public String toString() {
        return "KeywordMongo{" +
                "keyword='" + keyword + '\'' +
                ", habitIds=" + habitIds +
                '}';
    }
}
