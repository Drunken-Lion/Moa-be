package com.moa.moa.api.cs.question.domain;

import com.moa.moa.api.cs.question.domain.entity.Question;
import com.moa.moa.api.cs.question.domain.persistence.QuestionRepository;
import com.moa.moa.api.cs.question.util.enumerated.QuestionStatus;
import com.moa.moa.api.member.member.domain.entity.Member;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@RequiredArgsConstructor
public class QuestionProcessor {
    private final QuestionRepository questionRepository;

    public Slice<Question> findAllMyQuestion(Member member, Pageable pageable) {
        return questionRepository.findAllMyQuestion(member, pageable);
    }

    public Integer countMyQuestion(Member member) {
        return questionRepository.countByMember(member);
    }

    public Optional<Question> findQuestionById(Long id) {
        return questionRepository.findQuestionById(id);
    }

    public Question addQuestion(Question question, Member member, QuestionStatus status) {
        question = question.toBuilder()
                .member(member)
                .status(status)
                .build();

        return questionRepository.save(question);
    }

    public Question modQuestion(Question question) {
        return questionRepository.save(question);
    }
}
