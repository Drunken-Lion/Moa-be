package com.moa.moa.api.cs.question.application;

import com.moa.moa.api.cs.question.application.mapstruct.QuestionMapstructMapper;
import com.moa.moa.api.cs.question.domain.QuestionProcessor;
import com.moa.moa.api.cs.question.domain.dto.FindAllQuestionDto;
import com.moa.moa.api.cs.question.domain.entity.Question;
import com.moa.moa.api.cs.question.util.enumerated.QuestionStatus;
import com.moa.moa.api.cs.question.util.enumerated.QuestionType;
import com.moa.moa.api.member.member.domain.entity.Member;
import com.moa.moa.api.member.member.util.enumerated.MemberRole;
import com.moa.moa.global.common.response.PageExternalDto;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.SliceImpl;

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class QuestionServiceTest {
    @Mock
    private QuestionProcessor questionProcessor;

    @Mock
    private QuestionMapstructMapper questionMapstructMapper;

    @InjectMocks
    private QuestionService questionService;

    private final Long memberId = 1L;
    private final String memberEmail = "test1@moa.com";
    private final String memberNickname = "test1";
    private final MemberRole memberRole = MemberRole.MEMBER;
    private final LocalDateTime memberCreatedAt = LocalDateTime.now();

    private final Long questionId = 1L;
    private final QuestionType questionType = QuestionType.COMMON;
    private final String questionTitle = "사용자 문의 1";
    private final String questionContent = "사용자 문의 내용 1";
    private final QuestionStatus questionStatus = QuestionStatus.INCOMPLETE;
    private final LocalDateTime questionCreatedAt = LocalDateTime.now();

    private final int page = 0;
    private final int size = 10;
    private final boolean hasNext = false;

    @BeforeEach
    void beforeEach() {
    }

    @AfterEach
    void afterEach() {
    }

    @Test
    @DisplayName("나의 문의 내역 조회")
    void findAllQuestion() {
        // given
        Slice<Question> questionSlice = getQuestionSlice();
        Slice<FindAllQuestionDto.Response> questionResponse = questionSlice.map(this::mapper);
        PageExternalDto.Response<List<FindAllQuestionDto.Response>> pageResponse = pageMapper(questionResponse, PageRequest.of(page, size), 1);

        when(questionProcessor.findAllMyQuestion(any(), any(Pageable.class))).thenReturn(questionSlice);
        when(questionProcessor.countMyQuestion(any())).thenReturn(1);

        for (int i = 0; i < questionSlice.getContent().size(); i++) {
            when(questionMapstructMapper.of(
                    eq(questionSlice.getContent().get(i))
            )).thenReturn(questionResponse.getContent().get(i));
        }

        when(questionMapstructMapper.of(questionResponse, PageRequest.of(page, size), 1)).thenReturn(pageResponse);

        // when
        PageExternalDto.Response<List<FindAllQuestionDto.Response>> questions =
                questionService.findAllQuestion(getMember(), PageRequest.of(page, size));

        // then
        List<FindAllQuestionDto.Response> questionList = questions.data();
        assertThat(questionList.size()).isEqualTo(1);
        assertThat(questionList.get(0).id()).isEqualTo(questionId);
        assertThat(questionList.get(0).type()).isEqualTo(questionType);
        assertThat(questionList.get(0).title()).isEqualTo(questionTitle);
        assertThat(questionList.get(0).status()).isEqualTo(questionStatus);
        assertThat(questionList.get(0).createdAt()).isEqualTo(questionCreatedAt);
    }

    private Slice<Question> getQuestionSlice() {
        Pageable pageable = PageRequest.of(page, size);
        return new SliceImpl<>(List.of(getQuestion()), pageable, hasNext);
    }

    private Member getMember() {
        return Member.builder()
                .id(memberId)
                .email(memberEmail)
                .nickname(memberNickname)
                .role(memberRole)
                .createdAt(memberCreatedAt)
                .build();
    }

    private Question getQuestion() {
        return Question.builder()
                .id(questionId)
                .member(getMember())
                .title(questionTitle)
                .content(questionContent)
                .type(questionType)
                .status(questionStatus)
                .createdAt(questionCreatedAt)
                .build();
    }

    private FindAllQuestionDto.Response mapper(Question question) {
        return FindAllQuestionDto.Response.builder()
                .id(question.getId())
                .type(question.getType())
                .title(question.getTitle())
                .status(question.getStatus())
                .createdAt(question.getCreatedAt())
                .build();
    }

    private PageExternalDto.Response<List<FindAllQuestionDto.Response>> pageMapper(Slice<FindAllQuestionDto.Response> responses, Pageable pageable, Integer totalSize) {
        if (responses == null && pageable == null && totalSize == null) {
            return null;
        }

        List<FindAllQuestionDto.Response> data = responses.getContent();
        PageExternalDto.PageInfo pageInfo = new PageExternalDto.PageInfo(pageable.getPageNumber(), pageable.getPageSize(), responses.hasNext(), totalSize);

        PageExternalDto.Response<List<FindAllQuestionDto.Response>> response = new PageExternalDto.Response<>(data, pageInfo);

        return response;
    }
}
