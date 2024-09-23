package com.moa.moa.api.cs.question.application;

import com.moa.moa.api.cs.answer.domain.entity.Answer;
import com.moa.moa.api.cs.question.application.mapstruct.QuestionMapstructMapper;
import com.moa.moa.api.cs.question.application.mapstruct.QuestionMapstructMapperImpl;
import com.moa.moa.api.cs.question.domain.QuestionProcessor;
import com.moa.moa.api.cs.question.domain.dto.AddQuestionDto;
import com.moa.moa.api.cs.question.domain.dto.FindAllQuestionDto;
import com.moa.moa.api.cs.question.domain.dto.FindQuestionDto;
import com.moa.moa.api.cs.question.domain.dto.ModQuestionDto;
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
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

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
        AddQuestionDto.Request addRequest = AddQuestionDto.Request.builder()
                .type(questionType)
                .title(questionTitle)
                .content(questionContent)
                .build();

        Question requestQuestion = questionMapstructMapperImpl.addOf(addRequest, getMember(), QuestionStatus.INCOMPLETE);
        AddQuestionDto.Response addResponse = questionMapstructMapperImpl.addOf(getQuestion());

        when(questionProcessor.addQuestion(requestQuestion)).thenReturn(getQuestion());
        when(questionMapstructMapper.addOf(any(AddQuestionDto.Request.class), any(Member.class), any())).thenReturn(requestQuestion);
        when(questionMapstructMapper.addOf(any(Question.class))).thenReturn(addResponse);

        // when
        AddQuestionDto.Response response = questionService.addQuestion(addRequest, getMember());

        // then
        assertThat(response.id()).isEqualTo(questionId);
    }

    @Test
    @DisplayName("문의 수정")
    void modQuestionTest() {
        // given
        ModQuestionDto.Request modRequest = ModQuestionDto.Request.builder()
                .type(QuestionType.BUSINESS)
                .title("문의 수정 테스트 제목")
                .content("문의 수정 테스트 내용")
                .build();

        Question requestQuestion = questionMapstructMapperImpl.modOf(getQuestion(), modRequest);
        ModQuestionDto.Response modResponse = questionMapstructMapperImpl.modOf(requestQuestion);

        when(questionProcessor.findQuestionById(any())).thenReturn(Optional.of(getQuestion()));
        when(questionProcessor.modQuestion(requestQuestion)).thenReturn(requestQuestion);
        when(questionMapstructMapper.modOf(any(Question.class), any(ModQuestionDto.Request.class))).thenReturn(requestQuestion);
        when(questionMapstructMapper.modOf(any(Question.class))).thenReturn(modResponse);

        // when
        ModQuestionDto.Response response = questionService.modQuestion(getQuestion().getId(), modRequest, getMember());

        // then
        assertThat(response.id()).isEqualTo(questionId);
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
}
