package com.moa.moa.api.cs.question.application;

import com.moa.moa.api.cs.question.application.mapstruct.QuestionMapstructMapper;
import com.moa.moa.api.cs.question.domain.QuestionProcessor;
import com.moa.moa.api.cs.question.domain.dto.FindAllQuestionDto;
import com.moa.moa.api.cs.question.domain.entity.Question;
import com.moa.moa.api.member.member.domain.entity.Member;
import com.moa.moa.global.common.response.PageExternalDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;

import java.util.List;

@Log4j2
@Service
@RequiredArgsConstructor
public class QuestionService {
    private final QuestionProcessor questionProcessor;
    private final QuestionMapstructMapper questionMapstructMapper;

    public PageExternalDto.Response<List<FindAllQuestionDto.Response>> findAllQuestion(Member member,
                                                                                       Pageable pageable) {
        Slice<Question> questions = questionProcessor.findAllMyQuestion(member, pageable);
        Slice<FindAllQuestionDto.Response> findAllQuestionDtos = questions.map(questionMapstructMapper::of);

        log.info("슬라이스");
        log.info(questions);

        return questionMapstructMapper.of(findAllQuestionDtos, pageable, questionProcessor.countMyQuestion(member));
    }
}
