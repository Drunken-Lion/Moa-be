package com.moa.moa.api.cs.question.presentation.dto;

import com.moa.moa.api.cs.question.util.enumerated.QuestionStatus;
import com.moa.moa.api.cs.question.util.enumerated.QuestionType;
import lombok.Builder;

import java.time.LocalDateTime;

public record FindAllQuestionExternalDto() {
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
