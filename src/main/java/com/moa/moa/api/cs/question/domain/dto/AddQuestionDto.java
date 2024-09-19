package com.moa.moa.api.cs.question.domain.dto;

import com.moa.moa.api.cs.question.util.enumerated.QuestionType;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;

import java.util.List;

public record AddQuestionDto() {
    @Builder
    public record Request(
            @NotNull
            QuestionType type,
            @NotNull
            String title,
            @NotNull
            String content,
            List<String> images
    ) {
    }

    public record Response(Long id) {
    }
}
