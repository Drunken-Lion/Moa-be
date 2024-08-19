package com.moa.moa.api.cs.question.domain.persistence;

import com.moa.moa.api.cs.question.domain.entity.Question;
import com.moa.moa.api.cs.question.util.enumerated.QuestionStatus;
import com.moa.moa.api.cs.question.util.enumerated.QuestionType;
import com.moa.moa.api.member.member.domain.entity.Member;
import com.moa.moa.api.member.member.domain.persistence.MemberRepository;
import com.moa.moa.api.member.member.util.enumerated.MemberRole;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.IntStream;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@SpringBootTest
@AutoConfigureMockMvc(addFilters = false)
@ActiveProfiles("test")
@Transactional
public class QuestionRepositoryTest {
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
    void findAllQuestionTest() {
        // given
        Member member = memberRepository.findById(1L).get();
        Pageable pageable = PageRequest.of(0, 20);

        // when
        Slice<Question> questionSlice = questionRepository.findAllMyQuestion(member, pageable);

        // then
        assertThat(questionSlice.getNumber()).isEqualTo(0);
        assertThat(questionSlice.getSize()).isEqualTo(20);

        List<Question> questions = questionSlice.getContent();
        assertThat(questions).isInstanceOf(List.class);
        assertThat(questions.size()).isEqualTo(10);
        assertThat(questions.getFirst().getId()).isEqualTo(10);
        assertThat(questions.getFirst().getType()).isEqualTo(QuestionType.COMMON);
        assertThat(questions.getFirst().getTitle()).isEqualTo("사용자 문의 10");
        assertThat(questions.getFirst().getStatus()).isEqualTo(QuestionStatus.INCOMPLETE);
        assertThat(questions.getFirst().getCreatedAt()).isNotNull();
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
