package dev.be.oneday.controller;

import dev.be.oneday.config.JacksonConfig;
import dev.be.oneday.dto.UserAccountDto;
import dev.be.oneday.service.HabitJoinService;
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

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.willDoNothing;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@DisplayName("[컨트롤러] - 습관 참여")
@WebMvcTest(HabitJoinController.class)
@Import({JacksonConfig.class})
@MockBean(JpaMetamodelMappingContext.class)
class HabitJoinControllerTest {

    private final MockMvc mvc;

    @MockBean
    private HabitJoinService habitJoinService;
    @MockBean
    private UserAccountService userAccountService;

    public HabitJoinControllerTest( @Autowired MockMvc mvc ) { this.mvc = mvc; }

    @DisplayName("[POST] 습관아이디와 유저정보로 습관에 참여한다.")
    @Test
    void givenHabitIdAndUserInfo_whenJoin_thenJoinHabit() throws Exception{
        // given
        Long habitId = 1L;
        willDoNothing().given(habitJoinService).create(any(Long.class),any(UserAccountDto.class));

        // when & then
        mvc.perform(post("/habits/"+habitId+"/habit-join"))
                .andExpect(status().isOk());

    }

    @DisplayName("[GET] 습관 아이디로, 습관에 참여한 정보들을 반환한다.")
    @Test
    void givenHabitId_whenSearch_thenJoinedUsers() throws Exception{
        // given
        Long habitId = 1L;
        given(habitJoinService.getHabitUsers(any(Long.class), any(Pageable.class))).willReturn(Page.empty());

        // when & then
        mvc.perform(get("/habits/"+habitId+"/habit-join"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));
    }

    @DisplayName("[GET] 자신의 유저정보로, 가입한 습관들을 가져온다.")
    @Test
    void givenMyUserInfo_whenSearch_thenMyJoinedHabits() throws Exception{
        // given
        given(habitJoinService.getUserHabits(any(UserAccountDto.class),any(Pageable.class))).willReturn(Page.empty());

        // when & then
        mvc.perform(get("/mypage/habit-join"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));
    }
}