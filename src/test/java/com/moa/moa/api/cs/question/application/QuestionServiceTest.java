package com.moa.moa.api.cs.question.application;

import com.moa.moa.api.cs.answer.domain.entity.Answer;
import com.moa.moa.api.cs.question.application.mapstruct.QuestionMapstructMapper;
import com.moa.moa.api.cs.question.application.mapstruct.QuestionMapstructMapperImpl;
import com.moa.moa.api.cs.question.domain.QuestionProcessor;
import com.moa.moa.api.cs.question.domain.dto.AddQuestionDto;
import com.moa.moa.api.cs.question.domain.dto.FindAllQuestionDto;
import com.moa.moa.api.cs.question.domain.dto.FindQuestionDto;
import com.moa.moa.api.cs.question.domain.entity.Question;
import com.moa.moa.api.cs.question.util.enumerated.QuestionStatus;
import com.moa.moa.api.cs.question.util.enumerated.QuestionType;
import com.moa.moa.api.member.member.domain.entity.Member;
import com.moa.moa.api.member.member.util.enumerated.MemberRole;
import com.moa.moa.global.aws.s3.images.domain.entity.Image;
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
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class QuestionServiceTest {
    @Mock
    private QuestionProcessor questionProcessor;

    @Mock
    private QuestionMapstructMapper questionMapstructMapper;

    private QuestionMapstructMapperImpl questionMapstructMapperImpl;

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

    private final LocalDateTime answerCreatedAt = LocalDateTime.now();

    private final int page = 0;
    private final int size = 10;
    private final boolean hasNext = false;

    @BeforeEach
    void beforeEach() {
        questionMapstructMapperImpl = new QuestionMapstructMapperImpl();
    }

    @AfterEach
    void afterEach() {
    }

    @Test
    @DisplayName("나의 문의 내역 조회")
    void findAllQuestionTest() {
        // given
        Slice<Question> questionSlice = getQuestionSlice();
        Slice<FindAllQuestionDto.Response> questionResponse = questionSlice.map(questionMapstructMapperImpl::of);
        PageExternalDto.Response<List<FindAllQuestionDto.Response>> pageResponse = questionMapstructMapperImpl.of(questionResponse, PageRequest.of(page, size), 1);

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

    @Test
    @DisplayName("문의 상세 조회")
    void findQuestionTest() {
        // given
        FindQuestionDto.MemberResponse memberResponse = questionMapstructMapperImpl.of(getMember());
        FindQuestionDto.ImageResponse imageResponse = questionMapstructMapperImpl.of(getImage());
        List<FindQuestionDto.AnswerResponse> answerResponses = new ArrayList<>();

        when(questionProcessor.findQuestionById(any())).thenReturn(Optional.ofNullable(getQuestion()));
        when(questionMapstructMapper.of(any(Member.class))).thenReturn(memberResponse);
        when(questionMapstructMapper.of(any(Image.class))).thenReturn(imageResponse);

        for (Answer answer : getQuestion().getAnswers()) {
            FindQuestionDto.AnswerResponse answerResponse = questionMapstructMapperImpl.of(answer);
            answerResponses.add(answerResponse);
            when(questionMapstructMapper.of(answer)).thenReturn(answerResponse);
        }

        when(questionMapstructMapper.of(any(), any(), any(), any())).thenReturn(questionMapstructMapperImpl.of(getQuestion(), memberResponse, List.of(imageResponse), answerResponses));

        // when
        FindQuestionDto.Response question =
                questionService.findQuestion(getQuestion().getId(), getMember());

        // then
        assertThat(question.id()).isEqualTo(questionId);
        assertThat(question.type()).isEqualTo(questionType);
        assertThat(question.title()).isEqualTo(questionTitle);
        assertThat(question.status()).isEqualTo(questionStatus);
        assertThat(question.createdAt()).isEqualTo(questionCreatedAt);

        List<FindQuestionDto.AnswerResponse> answerList = question.answers();
        assertThat(answerList.size()).isEqualTo(answerResponses.size());
        assertThat(answerList.get(0).id()).isEqualTo(1L);
        assertThat(answerList.get(0).content()).isEqualTo("답변");
        assertThat(answerList.get(0).createdAt()).isEqualTo(answerCreatedAt);
    }

    @Test
    @DisplayName("문의 작성")
    void addQuestionTest() {
        // given
        AddQuestionDto.Request request = AddQuestionDto.Request.builder()
                .type(questionType)
                .title(questionTitle)
                .content(questionContent)
                .build();

        Question requestQuestion = questionMapstructMapperImpl.addOf(request);
        AddQuestionDto.Response response = questionMapstructMapperImpl.addOf(getQuestion());

        when(questionProcessor.addQuestion(requestQuestion, getMember(), QuestionStatus.INCOMPLETE)).thenReturn(getQuestion());
        when(questionMapstructMapper.addOf(any(AddQuestionDto.Request.class))).thenReturn(requestQuestion);
        when(questionMapstructMapper.addOf(any(Question.class))).thenReturn(response);

        // when
        AddQuestionDto.Response questionResponse = questionService.addQuestion(request, getMember());

        // then
        assertThat(questionResponse.id()).isEqualTo(questionId);
    }

    @Test
    @DisplayName("문의 삭제")
    void delQuestionTest() {
        // given
        Question mockQuestion = mock(Question.class);
        when(questionProcessor.findQuestionById(anyLong())).thenReturn(Optional.of(mockQuestion));
        when(mockQuestion.getMember()).thenReturn(getMember());

        doAnswer(invocation -> {
            when(mockQuestion.getDeletedAt()).thenReturn(LocalDateTime.now()); // 삭제 시간 설정
            return null;
        }).when(mockQuestion).modDeletedAt();

        // when
        questionService.delQuestion(questionId, getMember());

        // then
        verify(questionProcessor).findQuestionById(questionId);
        verify(mockQuestion, times(1)).modDeletedAt();

        assertThat(mockQuestion.getDeletedAt()).isNotNull();
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
                .answers(List.of(getAnswer()))
                .build();
    }

    private Image getImage() {
        return Image.builder().build();
    }

    private Answer getAnswer() {
        return Answer.builder()
                .id(1L)
                .content("답변")
                .createdAt(answerCreatedAt)
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
