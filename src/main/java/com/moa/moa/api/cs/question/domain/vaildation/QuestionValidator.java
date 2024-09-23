package com.moa.moa.api.cs.question.domain.vaildation;

import com.moa.moa.api.cs.question.domain.entity.Question;
import com.moa.moa.api.member.member.domain.entity.Member;
import com.moa.moa.global.common.message.FailHttpMessage;
import com.moa.moa.global.exception.BusinessException;

import java.util.Objects;

public class QuestionValidator {
    public static void validatePermission(Question question, Member member) {
        if (!Objects.equals(question.getMember(), member))
            throw new BusinessException(FailHttpMessage.Question.QUESTION_FORBIDDEN);
    }
}
