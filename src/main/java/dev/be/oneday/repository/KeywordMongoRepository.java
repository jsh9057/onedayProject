package dev.be.oneday.repository;

import dev.be.oneday.domain.KeywordMongo;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface KeywordMongoRepository extends MongoRepository<KeywordMongo, String> {

    boolean existsByKeyword(String keyword);
}
