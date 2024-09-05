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
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.IntStream;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@SpringBootTest
@ActiveProfiles("test")
@Transactional
public class QuestionRepositoryTest {
    @Autowired
    private MemberRepository memberRepository;
    @Autowired
    private QuestionRepository questionRepository;

    @BeforeEach
    void beforeEach() {
        List<Member> members = createMember();

        for (Member member : members) {
            if (member.getRole().equals(MemberRole.OWNER)) {
                IntStream.rangeClosed(1, 10).forEach(i -> {
                    QuestionStatus status = i % 2 == 0 ? QuestionStatus.INCOMPLETE : QuestionStatus.COMPLETE;
                    createQuestion(member, "렌탈샵 문의 제목 " + i, "렌탈샵 문의 내용 " + i, status);
                });
            } else {
                IntStream.rangeClosed(1, 10).forEach(i -> {
                    QuestionStatus status = i % 2 == 0 ? QuestionStatus.INCOMPLETE : QuestionStatus.COMPLETE;
                    createQuestion(member, "사용자 문의 제목 " + i, "사용자 문의 내용 " + i, status);
                });
            }
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
        Member member = memberRepository.findByEmail("three@moa.com").get();
        Pageable pageable = PageRequest.of(0, 20);

        // when
        Slice<Question> questionSlice = questionRepository.findAllMyQuestion(member, pageable);

        // then
        assertThat(questionSlice.getNumber()).isEqualTo(0);
        assertThat(questionSlice.getSize()).isEqualTo(20);

        List<Question> questions = questionSlice.getContent();
        assertThat(questions).isInstanceOf(List.class);
        assertThat(questions.size()).isEqualTo(10);
        assertThat(questions.getFirst().getType()).isEqualTo(QuestionType.COMMON);
        assertThat(questions.getFirst().getTitle()).isEqualTo("사용자 문의 제목 10");
        assertThat(questions.getFirst().getStatus()).isEqualTo(QuestionStatus.INCOMPLETE);
        assertThat(questions.getFirst().getCreatedAt()).isNotNull();
    }

    @Test
    @DisplayName("문의 내역 상세 조회")
    void findQuestionTest() {
        // given
        List<Question> questions = questionRepository.findAll();

        // when
        Question question = questionRepository.findQuestionById(questions.getFirst().getId()).get();

        // then
        assertThat(question.getId()).isEqualTo(1L);
        assertThat(question.getType()).isEqualTo(QuestionType.COMMON);
        assertThat(question.getTitle()).isEqualTo("사용자 문의 제목 1");
        assertThat(question.getContent()).isEqualTo("사용자 문의 내용 1");
        assertThat(question.getStatus()).isEqualTo(QuestionStatus.COMPLETE);
        assertThat(question.getCreatedAt()).isNotNull();
    }

    private List<Member> createMember() {
        List<Member> list = new ArrayList<>();

        list.add(Member.builder().email("admin@moa.com").nickname("admin").role(MemberRole.ADMIN).build());
        list.add(Member.builder().email("one@moa.com").nickname("one").role(MemberRole.MEMBER).build());
        list.add(Member.builder().email("two@moa.com").nickname("two").role(MemberRole.MEMBER).build());
        list.add(Member.builder().email("three@moa.com").nickname("three").role(MemberRole.MEMBER).build());
        list.add(Member.builder().email("four@moa.com").nickname("four").role(MemberRole.MEMBER).build());
        list.add(Member.builder().email("five@moa.com").nickname("five").role(MemberRole.MEMBER).build());
        list.add(Member.builder().email("owner@moa.com").nickname("owner").role(MemberRole.OWNER).build());

        memberRepository.saveAll(list);

        return list;
    }

    private void createQuestion(Member member, String title, String content, QuestionStatus status) {
        QuestionType type = member.getRole().equals(MemberRole.OWNER) ? QuestionType.BUSINESS : QuestionType.COMMON;

        Question question = Question.builder()
                .member(member)
                .title(title)
                .content(content)
                .type(type)
                .status(status)
                .build();

        questionRepository.save(question);
    }
}
