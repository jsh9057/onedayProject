package dev.be.oneday.service;

import dev.be.oneday.domain.KeywordMongo;
import dev.be.oneday.repository.KeywordMongoRepository;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.jpa.mapping.JpaMetamodelMappingContext;
import org.springframework.data.mongodb.core.MongoTemplate;

import java.util.ArrayList;

@Slf4j
@MockBean(JpaMetamodelMappingContext.class)
@DataMongoTest
class KeywordMongoServiceTest {

    @Autowired
    private MongoTemplate mongoTemplate;
    private final KeywordMongoRepository keywordMongoRepository;

    public KeywordMongoServiceTest(
            @Autowired KeywordMongoRepository keywordMongoRepository
    ){
        this.keywordMongoRepository = keywordMongoRepository;
    }

    @Test
    void saveTest(){
        KeywordMongo keywordMongo = KeywordMongo.builder()
                .keyword("영어")
                .habitIds(new ArrayList<>())
                .build();
        keywordMongo.addHabitId(1L);
        KeywordMongo insert = keywordMongoRepository.insert(keywordMongo);
        log.info("{}",insert);

    }

}