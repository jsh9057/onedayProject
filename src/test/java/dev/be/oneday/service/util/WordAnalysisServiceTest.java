package dev.be.oneday.service.util;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(MockitoExtension.class)
class WordAnalysisServiceTest {


    private WordAnalysisService wordAnalysisService = new WordAnalysisService();

    @Test
    void getNounsTest() throws Exception {
        // given
        String str = "매일 매일 한번 영어 영어 영어단어 외우기";
        String str2 = "매일 산책하기";
        String str3 = "하루 한 권 책읽기 ";

        // when
        List<String> nouns = wordAnalysisService.doWordNouns(str);
        List<String> nouns2 = wordAnalysisService.doWordNouns(str2);
        List<String> nouns3 = wordAnalysisService.doWordNouns(str3);

        // then
        assertThat(nouns).isNotNull();
        assertThat(nouns2).isNotNull();
        assertThat(nouns3).isNotNull();
    }

}