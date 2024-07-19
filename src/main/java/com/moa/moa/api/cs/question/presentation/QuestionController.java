package com.moa.moa.api.cs.question.presentation;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/questions")
@Tag(name = "v1-question", description = "문의 API")
public class QuestionController {
    @Operation(summary = "문의 목록 조회")
    @GetMapping
    public ResponseEntity<?> findAllQuestion() {
        return ResponseEntity.ok().body(null);
    }

    @Operation(summary = "문의 등록")
    @SecurityRequirement(name = "bearerAuth")
    @PostMapping
    public ResponseEntity<?> addQuestion() {
        return ResponseEntity.ok().body(null);
    }

    @Operation(summary = "문의 상세 조회")
    @GetMapping("{id}")
    public ResponseEntity<?> findQuestion(@PathVariable("id") Long id) {
        return ResponseEntity.ok().body(null);
    }

    @Operation(summary = "문의 수정")
    @SecurityRequirement(name = "bearerAuth")
    @PutMapping("{id}")
    public ResponseEntity<?> modQuestion(@PathVariable("id") Long id) {
        return ResponseEntity.ok().body(null);
    }

    @Operation(summary = "문의 삭제")
    @SecurityRequirement(name = "bearerAuth")
    @DeleteMapping("{id}")
    public ResponseEntity<?> delQuestion(@PathVariable("id") Long id) {
        return ResponseEntity.noContent().build();
    }

}
