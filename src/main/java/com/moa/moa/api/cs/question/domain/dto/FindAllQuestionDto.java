package com.moa.moa.api.cs.question.domain.dto;

import com.moa.moa.api.cs.question.util.enumerated.QuestionStatus;
import com.moa.moa.api.cs.question.util.enumerated.QuestionType;
import lombok.Builder;

import java.time.LocalDateTime;

public record FindAllQuestionDto() {
    @Builder
    public record Response(
            Long id,
            QuestionType type,
            String title,
            QuestionStatus status,
            LocalDateTime createdAt
    ) {
    }
}
