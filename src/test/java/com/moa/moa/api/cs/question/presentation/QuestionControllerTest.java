package com.moa.moa.api.cs.question.presentation;

import com.moa.moa.api.cs.question.domain.dto.AddQuestionDto;
import com.moa.moa.api.cs.question.domain.dto.ModQuestionDto;
import com.moa.moa.api.cs.question.domain.entity.Question;
import com.moa.moa.api.cs.question.domain.persistence.QuestionRepository;
import com.moa.moa.api.cs.question.util.enumerated.QuestionStatus;
import com.moa.moa.api.cs.question.util.enumerated.QuestionType;
import com.moa.moa.api.member.member.domain.entity.Member;
import com.moa.moa.api.member.member.domain.persistence.MemberRepository;
import com.moa.moa.api.member.member.util.enumerated.MemberRole;
import com.moa.moa.global.common.util.JsonConvertor;
import com.moa.moa.global.util.TestUtil;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.transaction.annotation.Transactional;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.stream.IntStream;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc(addFilters = false)
@ActiveProfiles("test")
@Transactional
public class QuestionControllerTest {
    @Autowired
    private MockMvc mvc;

    @Autowired
    private MemberRepository memberRepository;
    @Autowired
    private QuestionRepository questionRepository;

    @BeforeEach
    void beforeEach() {
        if (memberRepository.count() == 0) {
            Member member1 = memberRepository.save(createMember("test1@moa.com", MemberRole.ADMIN));
            Member member2 = memberRepository.save(createMember("test2@moa.com", MemberRole.OWNER));
            Member member3 = memberRepository.save(createMember("test3@moa.com", MemberRole.MEMBER));
            Member member4 = memberRepository.save(createMember("three@moa.com", MemberRole.MEMBER));

            IntStream.rangeClosed(1, 10).forEach(i -> {
                QuestionStatus status = i % 2 == 0 ? QuestionStatus.INCOMPLETE : QuestionStatus.COMPLETE;
                questionRepository.save(
                        createQuestion(member4, "사용자 문의 " + i, "사용자 문의 내용 " + i, status)
                );
            });

            IntStream.rangeClosed(1, 5).forEach(i -> {
                QuestionStatus status = i % 2 == 0 ? QuestionStatus.INCOMPLETE : QuestionStatus.COMPLETE;
                questionRepository.save(
                        createQuestion(member2, "렌탈샵 문의 " + i, "렌탈샵 문의 내용 " + i, status)
                );
            });
        }
    }

    @AfterEach
    void afterEach() {
        memberRepository.deleteAll();
        questionRepository.deleteAll();
    }

    @Test
    @DisplayName("나의 문의 내역 조회")
    void findAllQuestionTest() throws Exception {
        ResultActions actions = mvc
                .perform(get("/v1/questions")
                        .param("page", "0")
                        .param("size", "10"))
                .andDo(print());

        actions.andExpect(status().isOk())
                .andExpect(handler().handlerType(QuestionController.class))
                .andExpect(handler().methodName("findAllQuestion"))
                .andExpect(jsonPath("$.length()", is(2)))

                .andExpect(jsonPath("$.data.length()", is(10)))
                .andExpect(jsonPath("$.data[0].id", instanceOf(Number.class)))
                .andExpect(jsonPath("$.data[0].type", is(QuestionType.COMMON.toString())))
                .andExpect(jsonPath("$.data[0].title", is("사용자 문의 10")))
                .andExpect(jsonPath("$.data[0].status", is(QuestionStatus.INCOMPLETE.toString())))
                .andExpect(jsonPath("$.data[0].createdAt", matchesPattern(TestUtil.DATETIME_PATTERN)))

                .andExpect(jsonPath("$.pageInfo", instanceOf(LinkedHashMap.class)))
                .andExpect(jsonPath("$.pageInfo.page", is(0)))
                .andExpect(jsonPath("$.pageInfo.size", is(10)))
                .andExpect(jsonPath("$.pageInfo.hasNext", is(false)))
                .andExpect(jsonPath("$.pageInfo.totalSize", is(10)));
    }

    @Test
    @DisplayName("나의 문의 내역 상세 조회")
    void findQuestionTest() throws Exception {
        Member member = memberRepository.findByEmail("three@moa.com").get();

        Question question = Question.builder()
                .member(member)
                .type(QuestionType.COMMON)
                .title("제목 테스트")
                .content("내용 테스트")
                .status(QuestionStatus.INCOMPLETE)
                .build();

        questionRepository.save(question);

        ResultActions actions = mvc
                .perform(get("/v1/questions/" + question.getId()))
                .andDo(print());

        actions.andExpect(status().isOk())
                .andExpect(handler().handlerType(QuestionController.class))
                .andExpect(handler().methodName("findQuestion"))
                .andExpect(jsonPath("$.id", instanceOf(Number.class)))
                .andExpect(jsonPath("$.type", is(QuestionType.COMMON.toString())))
                .andExpect(jsonPath("$.title", is("제목 테스트")))
                .andExpect(jsonPath("$.content", is("내용 테스트")))
                .andExpect(jsonPath("$.status", is(QuestionStatus.INCOMPLETE.toString())))
                .andExpect(jsonPath("$.createdAt", matchesPattern(TestUtil.DATETIME_PATTERN)))

                .andExpect(jsonPath("$.member.id", instanceOf(Number.class)))
                .andExpect(jsonPath("$.member.email", is("three@moa.com")))
                .andExpect(jsonPath("$.member.nickname", is("three")))

                .andExpect(jsonPath("$.images", instanceOf(List.class)))
                .andExpect(jsonPath("$.answers", instanceOf(List.class)));
    }

    @Test
    @DisplayName("문의 작성")
    void addQuestionTest() throws Exception {
        Member member = memberRepository.findByEmail("three@moa.com").get();

        AddQuestionDto.Request request = AddQuestionDto.Request.builder()
                .type(QuestionType.COMMON)
                .title("문의 작성 테스트 제목")
                .content("문의 작성 테스트 내용")
                .build();

        ResultActions actions = mvc
                .perform(post("/v1/questions")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(JsonConvertor.build(request))
                )
                .andDo(print());

        Question addQuestion = questionRepository.findAll().getLast();

        actions.andExpect(status().isCreated())
                .andExpect(handler().handlerType(QuestionController.class))
                .andExpect(handler().methodName("addQuestion"))
                .andExpect(jsonPath("$.id", instanceOf(Number.class)));

        assertThat(addQuestion.getId()).isEqualTo(addQuestion.getId());
    }

    @Test
    @DisplayName("문의 수정")
    void modQuestionTest() throws Exception {
        Member member = memberRepository.findByEmail("three@moa.com").get();
        Question question = questionRepository.findAllMyQuestion(
                member, PageRequest.of(0, 20)).getContent().getFirst();

        ModQuestionDto.Request request = ModQuestionDto.Request.builder()
                .type(QuestionType.BUSINESS)
                .title("문의 수정 테스트 제목")
                .content("문의 수정 테스트 내용")
                .build();

        ResultActions actions = mvc
                .perform(put("/v1/questions/" + question.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(JsonConvertor.build(request))
                )
                .andDo(print());

        Question modQuestion = questionRepository.findQuestionById(question.getId()).get();

        actions.andExpect(status().isOk())
                .andExpect(handler().handlerType(QuestionController.class))
                .andExpect(handler().methodName("modQuestion"))
                .andExpect(jsonPath("$.id", instanceOf(Number.class)));

        assertThat(modQuestion.getId()).isEqualTo(question.getId());
        assertThat(modQuestion.getType()).isEqualTo(request.type());
        assertThat(modQuestion.getTitle()).isEqualTo(request.title());
        assertThat(modQuestion.getContent()).isEqualTo(request.content());
    }

    private Member createMember(String email, MemberRole role) {
        return Member.builder()
                .email(email)
                .nickname(email.substring(0, email.indexOf("@")))
                .role(role)
                .build();
    }

    private Question createQuestion(Member member, String title, String content, QuestionStatus status) {
        QuestionType type = member.getRole().equals(MemberRole.OWNER) ? QuestionType.BUSINESS : QuestionType.COMMON;

        return Question.builder()
                .member(member)
                .title(title)
                .content(content)
                .type(type)
                .status(status)
                .build();
    }
}
