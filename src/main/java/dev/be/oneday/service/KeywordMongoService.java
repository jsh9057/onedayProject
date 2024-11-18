package dev.be.oneday.service;

import dev.be.oneday.domain.KeywordMongo;
import dev.be.oneday.dto.HabitDto;
import dev.be.oneday.repository.HabitRepository;
import dev.be.oneday.service.util.WordAnalysisService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Service;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;
import org.springframework.transaction.support.TransactionSynchronizationManager;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class KeywordMongoService {
    private final MongoTemplate mongoTemplate;
    private final HabitRepository habitRepository;
    private final WordAnalysisService wordAnalysisService;

    private final String HABIT_IDS = "habitIds";

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void analysisAndSave(String title, Long habitId){
        TransactionStatus transactionStatus = TransactionAspectSupport.currentTransactionStatus();
        log.debug("isNewTransaction: "+transactionStatus.isNewTransaction());
        log.debug("TransactionName:"+ TransactionSynchronizationManager.getCurrentTransactionName());

        HashSet<String> nonDuplication = new HashSet<>(wordAnalysisService.doWordNouns(title));
        for (String keyword :nonDuplication){
            insert(keyword, habitId);
        }
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void analysisAndUpdate(String before, String after, Long habitId){
        HashSet<String> beforeKeyword = new HashSet<>(wordAnalysisService.doWordNouns(before));
        HashSet<String> afterKeyword = new HashSet<>(wordAnalysisService.doWordNouns(after));

        Set<String> delete = beforeKeyword.stream()
                .filter(i -> !afterKeyword.contains(i))
                .collect(Collectors.toSet());
        Set<String> insert = afterKeyword.stream()
                .filter(i -> !beforeKeyword.contains(i))
                .collect(Collectors.toSet());

        for (String b: delete){ delete(b,habitId); }
        for (String a: insert){ insert(a,habitId); }
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void analysisAndDelete(String title, Long habitId){
        HashSet<String> keywords = new HashSet<>(wordAnalysisService.doWordNouns(title));
        for (String keyword :keywords){
            delete(keyword,habitId);
        }
    }

    @Transactional(readOnly = true)
    public Page<HabitDto> searchForKeyword(String title, Pageable pageable){
        HashSet<String> keywords = new HashSet<>(wordAnalysisService.doWordNouns(title));
        Query query = new Query();
        query.with(pageable);

        List<Criteria> list = new ArrayList<>();
        for (String keyword : keywords){
            list.add(Criteria.where("_id").is(keyword));
        }
        Criteria criteria = Criteria.where("").orOperator(list);
        query.addCriteria(criteria);

        List<Long> result = mongoTemplate.findDistinct(query,HABIT_IDS, KeywordMongo.class,Long.class);
        log.debug("{}",result);

        Page<HabitDto> pageHabitDto = new PageImpl<>(habitRepository.findHabitIds(result,pageable).stream().map(HabitDto::from).collect(Collectors.toList()));
        return pageHabitDto;
    }

    public List<KeywordMongo> findAllKeyword(){
        return mongoTemplate.findAll(KeywordMongo.class);
    }

    public void delete(String keyword, Long habitId){
        Query query = new Query(Criteria.where("_id").is(keyword));
        int size = mongoTemplate.find(query, KeywordMongo.class).size();
        pullKeywordByHabitIds(keyword,habitId);
        if(size == 1){
            mongoTemplate.remove(query,KeywordMongo.class);
        }
    }

    public void deleteAll(){
        mongoTemplate.dropCollection(KeywordMongo.class);
    }

    public void insert(String keyword, Long habitId){
        Query query = new Query(Criteria.where("_id").is(keyword));
        boolean exists = mongoTemplate.exists(query, KeywordMongo.class);
        if(exists){
            pushKeywordByHabitIds(keyword, habitId);
        }
        else{
            mongoTemplate.insert(
                    KeywordMongo.builder()
                            .keyword(keyword)
                            .habitIds(List.of(habitId))
                            .build()
            );
        }

    }

    public void pushKeywordByHabitIds(String keyword, Long habitId){
        Query query = new Query(Criteria.where("_id").is(keyword));
        Update update = new Update();
        update.push(HABIT_IDS,habitId);
        mongoTemplate.updateFirst(query,update,KeywordMongo.class);
    }

    public void pullKeywordByHabitIds(String keyword, Long habitId){
        Query query = new Query(Criteria.where("_id").is(keyword));
        Update update = new Update();
        update.pull(HABIT_IDS,habitId);
        mongoTemplate.updateFirst(query, update, KeywordMongo.class);
    }
}
