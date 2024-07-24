package com.moa.moa.api.cs.question.presentation.dto;

import com.moa.moa.api.cs.question.util.enumerated.QuestionType;
import jakarta.validation.constraints.NotNull;

import java.util.List;

public record ModQuestionExternalDto() {
    public record Request(
            @NotNull
            QuestionType type,
            @NotNull
            String title,
            @NotNull
            String content,
            List<Integer> remove,
            List<String> images
    ) {
    }

    public record Response(Long id) {
    }
}
