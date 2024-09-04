package dev.be.oneday.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import dev.be.oneday.config.JacksonConfig;
import dev.be.oneday.dto.HabitDto;
import dev.be.oneday.dto.Request.HabitRequest;
import dev.be.oneday.dto.UserAccountDto;
import dev.be.oneday.service.HabitService;
import dev.be.oneday.service.UserAccountService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.mapping.JpaMetamodelMappingContext;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.equalTo;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.willDoNothing;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@DisplayName("[컨트롤러] - 습관")
@WebMvcTest(HabitController.class)
@Import({JacksonConfig.class})
@MockBean(JpaMetamodelMappingContext.class)
class HabitControllerTest {

    private final MockMvc mvc;

    @MockBean
    private HabitService habitService;
    @MockBean
    private UserAccountService userAccountService;

    @Autowired
    private ObjectMapper objectMapper;

    public HabitControllerTest( @Autowired MockMvc mvc ){
        this.mvc = mvc;
    }

    @DisplayName("[GET] 습관 id로 조회시, 습관을 반환한다.")
    @Test
    void givenHabitId_whenRequestHabit_thenReturnHabit() throws Exception{
        // given
        Long habitId = 1L;
        HabitDto habitDto = HabitDto.builder()
                .habitId(habitId)
                .userAccountDto(UserAccountDto.builder()
                        .nickname("testNickname")
                        .build())
                .title("testTitle")
                .content("testContent")
                .build();
        given(habitService.getHabit(eq(habitId))).willReturn(habitDto);

        // when & then
        mvc.perform(get("/habits/"+habitId))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.title",equalTo("testTitle")))
                .andExpect(jsonPath("$.content",equalTo("testContent")))
                .andDo(print());
    }

    @DisplayName("[GET] 습관 전체 조회시, Page 로 감싼 습관들을 반환한다.")
    @Test
    void givenPageable_whenRequestAllHabits_thenReturnPageHabits() throws Exception{
        // given
        given(habitService.getAllHabits(any(Pageable.class))).willReturn(Page.empty());

        // when & then
        mvc.perform(get("/habits"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));
    }

    @DisplayName("[POST] 습관 정보로 습관 생성시, 생성된 습관정보를 반환한다.")
    @Test
    void givenHabitInfo_whenCreat_thenSavedHabitInfo() throws Exception{
        // given
        HabitRequest habitRequest = HabitRequest.builder()
                .title("newTitle")
                .content("newContent")
                .build();
        HabitDto habitDto = habitRequest.toDto(UserAccountDto.builder().build());
        given(habitService.create(any(HabitDto.class))).willReturn(habitDto);
        String content = objectMapper.writeValueAsString(habitDto);

        // when & then
        mvc.perform(post("/habits")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(content))
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.title",equalTo("newTitle")))
                .andExpect(jsonPath("$.content",equalTo("newContent")))
                .andDo(print());
    }

    @DisplayName("[POST] 습관 정보로 수정 요청시, 수정된 습관정보를 반환한다.")
    @Test
    void givenHabitInfo_whenUpdate_thenUpdatedHabitInfo() throws Exception{
        // given
        HabitRequest habitRequest = HabitRequest.builder()
                .title("updateTitle")
                .content("updateContent")
                .build();
        HabitDto habitDto = habitRequest.toDto(UserAccountDto.builder().build());
        given(habitService.update(any(Long.class),any(HabitDto.class))).willReturn(habitDto);
        String content = objectMapper.writeValueAsString(habitDto);

        // when & then
        mvc.perform(put("/habits/"+1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(content))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.title",equalTo("updateTitle")))
                .andExpect(jsonPath("$.content",equalTo("updateContent")))
                .andDo(print());
    }

    @DisplayName("[DELETE] 습관 아이디로 삭제 요청시, 삭제 한다.")
    @Test
    void givenHabitId_whenDeletes_thenDeletesHabit() throws Exception{
        // given
        Long habitId = 1L;
        willDoNothing().given(habitService).delete(habitId);

        // when & then
        mvc.perform(delete("/habits/"+habitId))
                .andExpect(status().isOk())
                .andDo(print());
    }

}