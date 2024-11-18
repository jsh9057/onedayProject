package dev.be.oneday.service.util;

import kr.co.shineware.nlp.komoran.constant.DEFAULT_MODEL;
import kr.co.shineware.nlp.komoran.core.Komoran;
import kr.co.shineware.nlp.komoran.model.KomoranResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
public class WordAnalysisService {

    //자연어 처리 - 형태소 분석기인 Komoran를 메모리에 올리기 위해 WordAnalysisService 클래스 내 전역 변수로 설정합니다.
    Komoran nlp = null;

    //생성자 사용함 - 톰켓에서 부팅할 때 @Service를 모두 메모리에 올립니다.
    //톰켓이 메모리에 올릴 때, 생성자에 선언한 Komoran도 같이 메모리에 올라가도록 생성자에 코딩합니다.
    //생성자에서 Komoran을 메모리에 올리면, 매번 메모리에 올려서 호출하는 것이 아니라,
    // 메모리에 올리간 객체만 불러와서 사용할 수 있기 때문에 처리 속도가 빠릅니다.
    public WordAnalysisService() {

        log.debug(this.getClass().getName() + ".WordAnalysisService creator Start !");

        //NLP 분석 객체 메모리 로딩합니다.
        this.nlp = new Komoran(DEFAULT_MODEL.LIGHT); // 학습데이터 경량화 버전( 웹 서비스에 적합합니다. )
        //this.nlp = new Komoran(DEFAULT_MODEL.FULL); // 학습데이터 전체 버전(일괄처리 : 배치 서비스에 적합합니다.)

//        log.debug("난 톰켓이 부팅되면서 스프링 프렝미워크가 자동 실행되었고, 스프링 실행될 때 nlp 변수에 Komoran 객체를 생성하여 저장하였다.");

        log.debug(this.getClass().getName() + ".WordAnalysisService creator End !");


    }

    public List<String> doWordNouns(String text) {
        log.debug(this.getClass().getName() + ".doWordAnalysis Start !");
        log.debug("분석할 문장 : " + text);

        //분석할 문장에 대해 정제(쓸데없는 특수문자 제거)
        String replace_text = text.replace("[^가-힣a-zA-Z0-9", " ");

//        log.info("한국어, 영어, 숫자 제외 단어 모두 한 칸으로 변환시킨 문장 : " + replace_text);

        //분석할 문장의 앞, 뒤에 존재할 수 있는 필요없는 공백 제거
        String trim_text = replace_text.trim();

//        log.info("분석할 문장 앞, 뒤에 존재할 수 있는 필요 없는 공백 제거 : " + trim_text);

        //형태소 분석 시작
        KomoranResult analyzeResultList = this.nlp.analyze(trim_text);
        //형태소 분석 결과 중 명사만 가져오기
        List<String> rList = analyzeResultList.getNouns();

        log.debug("nouns : {}",rList);

        if (rList == null) { rList = new ArrayList<String>();}
        return rList;
    }
}
