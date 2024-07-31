package com.moa.moa.api.cs.question.presentation.dto;

import jakarta.validation.constraints.NotNull;
import java.util.List;

public record ModQuestionExternalDto() {
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
