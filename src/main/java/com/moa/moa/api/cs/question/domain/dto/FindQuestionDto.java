package com.moa.moa.api.cs.question.domain.dto;

import com.moa.moa.api.cs.question.util.enumerated.QuestionStatus;
import com.moa.moa.api.cs.question.util.enumerated.QuestionType;
import lombok.Builder;

import java.time.LocalDateTime;
import java.util.List;

public record FindQuestionDto() {
    @Builder
    public record Response(
            Long id,
            QuestionType type,
            String title,
            String content,
            QuestionStatus status,
            LocalDateTime createdAt,

            MemberResponse member,
            List<ImageResponse> images,
            List<AnswerResponse> answers
    ) {
    }

    @Builder
    public record MemberResponse(
            Long id,
            String email,
            String nickname
    ) {
    }

    @Builder
    public record ImageResponse(
            Long id,
            String originImageUrl,
            String lowImageUrl,
            LocalDateTime createdAt
    ) {
    }

    @Builder
    public record AnswerResponse(
            Long id,
            String content,
            LocalDateTime createdAt
    ) {
    }
}
