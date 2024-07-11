package com.moa.moa.api.answer.domain.entity;

import com.moa.moa.api.member.domain.entity.Member;
import com.moa.moa.api.question.domain.entity.Question;

public class Answer {
    private String content;
    private Member member;
    private Question question;
}
