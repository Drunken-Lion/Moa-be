package com.moa.moa.api.cs.question.application;

import com.moa.moa.api.cs.question.application.mapstruct.QuestionMapstructMapper;
import com.moa.moa.api.cs.question.domain.QuestionProcessor;
import com.moa.moa.api.cs.question.domain.dto.AddQuestionDto;
import com.moa.moa.api.cs.question.domain.dto.FindAllQuestionDto;
import com.moa.moa.api.cs.question.domain.dto.FindQuestionDto;
import com.moa.moa.api.cs.question.domain.entity.Question;
import com.moa.moa.api.cs.question.util.enumerated.QuestionStatus;
import com.moa.moa.api.member.member.domain.entity.Member;
import com.moa.moa.global.aws.s3.images.domain.entity.Image;
import com.moa.moa.global.common.message.FailHttpMessage;
import com.moa.moa.global.common.response.PageExternalDto;
import com.moa.moa.global.exception.BusinessException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class QuestionService {
    private final QuestionProcessor questionProcessor;
    private final QuestionMapstructMapper questionMapstructMapper;

    public PageExternalDto.Response<List<FindAllQuestionDto.Response>> findAllQuestion(Member member,
                                                                                       Pageable pageable) {
        Slice<Question> questions = questionProcessor.findAllMyQuestion(member, pageable);
        Slice<FindAllQuestionDto.Response> findAllQuestionDtos = questions.map(questionMapstructMapper::of);

        return questionMapstructMapper.of(findAllQuestionDtos, pageable, questionProcessor.countMyQuestion(member));
    }

    public FindQuestionDto.Response findQuestion(Long id,
                                                 Member member) {
        Question question = questionProcessor.findQuestionById(id)
                .orElseThrow(() -> new BusinessException(FailHttpMessage.Question.QUESTION_NOT_FOUND));

        if (!question.getMember().equals(member))
            throw new BusinessException(FailHttpMessage.Question.QUESTION_FORBIDDEN);

        FindQuestionDto.MemberResponse memberResponse = questionMapstructMapper.of(question.getMember());
        FindQuestionDto.ImageResponse imageResponse = questionMapstructMapper.of(Image.builder().build());
        List<FindQuestionDto.AnswerResponse> answerResponses =
                question.getAnswers().stream().map(questionMapstructMapper::of).toList();

        // TODO : 이미지 기능이 완료되면 수정
        return questionMapstructMapper.of(question, memberResponse, List.of(imageResponse), answerResponses);
    }

    public AddQuestionDto.Response addQuestion(AddQuestionDto.Request request, Member member) {
        Question question = questionProcessor.addQuestion(
                questionMapstructMapper.addOf(request), member, QuestionStatus.INCOMPLETE);


        // TODO : 이미지 기능이 완료되면 수정
        
        return questionMapstructMapper.addOf(question);
    }
}
