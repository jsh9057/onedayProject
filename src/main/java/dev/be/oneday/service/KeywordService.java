package dev.be.oneday.service;

import dev.be.oneday.domain.Keyword;
import dev.be.oneday.dto.HabitDto;
import dev.be.oneday.dto.KeywordDto;
import dev.be.oneday.exception.BaseException;
import dev.be.oneday.exception.ErrorType;
import dev.be.oneday.repository.HabitRepository;
import dev.be.oneday.repository.KeywordRepository;
import dev.be.oneday.service.util.WordAnalysisService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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
public class KeywordService {
    private final KeywordRepository keywordRepository;
    private final HabitRepository habitRepository;
    private final WordAnalysisService wordAnalysisService;

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void create(HabitDto habitDto) {
        TransactionStatus transactionStatus = TransactionAspectSupport.currentTransactionStatus();
        log.info("isNewTransaction: "+transactionStatus.isNewTransaction());
        log.info("TransactionName:"+TransactionSynchronizationManager.getCurrentTransactionName());
        Set<String> keywords;
        try {
            keywords = new HashSet<>(wordAnalysisService.doWordNouns(habitDto.getTitle()));
        } catch (Exception e) {
            throw new BaseException(ErrorType.INTERNAL_SERVER_ERROR, "keyword 추출 실패 title:" + habitDto.getTitle());
        }
        if(keywords.isEmpty()){
            log.info("title 에 명사가 없습니다.");
            return;
        }

        keywordRepository.saveAll(keywords.stream()
                .map(keyword -> Keyword.builder()
                        .keyword(keyword)
                        .habitId(habitDto.getHabitId())
                        .build()
                ).collect(Collectors.toList()));
    }

    @Transactional(readOnly = true)
    public Page<HabitDto> searchForKeyword(String inputTitle, Pageable pageable){
        Set<String> keywords;

        if(inputTitle.isBlank()){
            throw new BaseException(ErrorType.INVALID_PARAMETER,"검색어가 비어있습니다.");
        }

        keywords = new HashSet<>(wordAnalysisService.doWordNouns(inputTitle));
        List<Long> habitIds = List.of();
        try {
            habitIds = keywordRepository.findKeywordsIn(keywords, pageable);
        }catch (Exception e){
            throw new BaseException(ErrorType.INTERNAL_SERVER_ERROR, "keyword 매핑 실패");
        }
        if(habitIds.isEmpty()){
            log.info("검색어와 일치하는 습관이 없습니다. ");
            return Page.empty();
        }

        return habitRepository.findHabitIds(habitIds,pageable)
                .map(HabitDto::from);
    }

    @Transactional
    public void updateKeyword(Long habitId, String before, String after) {
        if(after == null ||  after.equals(before)){ return; }
        Set<String> beforeKeywords = new HashSet<>(wordAnalysisService.doWordNouns(before));
        Set<String> afterKeywords = new HashSet<>(wordAnalysisService.doWordNouns(after));
        List<String> insert = insertKeywords(beforeKeywords,afterKeywords);
        List<String> delete = deleteKeywords(beforeKeywords,afterKeywords);

        keywordRepository.saveAll(insert.stream()
                .map(keyword -> Keyword.builder()
                        .keyword(keyword)
                        .habitId(habitId)
                        .build()
                ).collect(Collectors.toList()));
        keywordRepository.deleteKeywords(new HashSet<>(delete), habitId);
    }

    @Transactional
    public void deleteHabitKeyword(Long habitId){
        keywordRepository.deleteAllByHabitId(habitId);
    }

    @Transactional(readOnly = true)
    public List<KeywordDto> findAllKeyword(){ return keywordRepository.findAll().stream().map(KeywordDto::from).toList(); }


    public List<String> insertKeywords(Set<String> before, Set<String> after){
        List<String> list = new ArrayList<>();
        for (String a :after){
            if(!before.contains(a)){
                list.add(a);
            }
        }
        return list;
    }

    public List<String> deleteKeywords(Set<String> before, Set<String> after){
        List<String> list = new ArrayList<>();
        for (String b : before){
            if(!after.contains(b)){
                list.add(b);
            }
        }
        return list;
    }
}
