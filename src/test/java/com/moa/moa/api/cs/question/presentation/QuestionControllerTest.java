package com.moa.moa.api.cs.question.presentation;

import com.moa.moa.api.cs.question.domain.entity.Question;
import com.moa.moa.api.cs.question.domain.persistence.QuestionRepository;
import com.moa.moa.api.cs.question.util.enumerated.QuestionStatus;
import com.moa.moa.api.cs.question.util.enumerated.QuestionType;
import com.moa.moa.api.member.member.domain.entity.Member;
import com.moa.moa.api.member.member.domain.persistence.MemberRepository;
import com.moa.moa.api.member.member.util.enumerated.MemberRole;
import com.moa.moa.global.util.TestUtil;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.transaction.annotation.Transactional;

import java.util.LinkedHashMap;
import java.util.stream.IntStream;

import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
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
            Member member1 = memberRepository.save(createMember("test1@moa.com", MemberRole.MEMBER));
            Member member2 = memberRepository.save(createMember("test2@moa.com", MemberRole.OWNER));

            IntStream.rangeClosed(1, 10).forEach(i -> {
                QuestionStatus status = i % 2 == 0 ? QuestionStatus.INCOMPLETE : QuestionStatus.COMPLETE;
                questionRepository.save(
                        createQuestion(member1, "사용자 문의 " + i, "사용자 문의 내용 " + i, status)
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
                .andExpect(jsonPath("$.pageInfo.totalSize", is(10)))
        ;
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
