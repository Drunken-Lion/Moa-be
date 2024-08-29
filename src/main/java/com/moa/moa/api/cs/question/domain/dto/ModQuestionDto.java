package com.moa.moa.api.cs.question.domain.dto;

import jakarta.validation.constraints.NotNull;

import java.util.List;

public record ModQuestionDto() {
    public record Request(
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
