package com.moa.moa.api.cs.answer.domain.entity;

import com.moa.moa.api.member.member.domain.entity.Member;
import com.moa.moa.api.cs.question.domain.entity.Question;

public class Answer {
    private Question question;
    private Member member;
    private String content;
}
