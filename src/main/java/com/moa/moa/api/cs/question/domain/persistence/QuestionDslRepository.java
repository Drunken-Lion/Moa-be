package com.moa.moa.api.cs.question.domain.persistence;

import com.moa.moa.api.cs.question.domain.entity.Question;
import com.moa.moa.api.member.member.domain.entity.Member;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;

public interface QuestionDslRepository {
    Slice<Question> findAllMyQuestion(Member authMember, Pageable pageable);
}
