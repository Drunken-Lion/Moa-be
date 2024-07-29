package com.moa.moa.api.cs.question.presentation;

import com.moa.moa.api.cs.question.presentation.dto.AddQuestionExternalDto;
import com.moa.moa.api.cs.question.presentation.dto.FindQuestionExternalDto;
import com.moa.moa.api.cs.question.presentation.dto.ModQuestionExternalDto;
import com.moa.moa.api.shop.review.presentation.dto.FindAllReviewExternalDto;
import com.moa.moa.global.common.response.PageExternalDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.nio.file.attribute.UserPrincipal;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/questions")
@Tag(name = "Question-API", description = "문의 API")
public class QuestionController {
    @Operation(summary = "문의 목록 조회")
    @GetMapping
    public ResponseEntity<PageExternalDto.Response<List<FindAllReviewExternalDto.Response>>> findAllQuestion(@AuthenticationPrincipal UserPrincipal user) {
        return ResponseEntity.ok().body(null);
    }

    @Operation(summary = "문의 등록")
    @SecurityRequirement(name = "bearerAuth")
    @PostMapping
    public ResponseEntity<AddQuestionExternalDto.Response> addQuestion(@AuthenticationPrincipal UserPrincipal user,
                                                                       @Valid @RequestBody final AddQuestionExternalDto.Request request) {
        return ResponseEntity.ok().body(null);
    }

    @Operation(summary = "문의 상세 조회")
    @GetMapping("{id}")
    public ResponseEntity<FindQuestionExternalDto.Response> findQuestion(@PathVariable("id") Long id,
                                                                         @AuthenticationPrincipal UserPrincipal user) {
        return ResponseEntity.ok().body(null);
    }

    @Operation(summary = "문의 수정")
    @SecurityRequirement(name = "bearerAuth")
    @PutMapping("{id}")
    public ResponseEntity<ModQuestionExternalDto.Response> modQuestion(@PathVariable("id") Long id,
                                                                       @AuthenticationPrincipal UserPrincipal user,
                                                                       @Valid @RequestBody final ModQuestionExternalDto.Request request) {
        return ResponseEntity.ok().body(null);
    }

    @Operation(summary = "문의 삭제")
    @SecurityRequirement(name = "bearerAuth")
    @DeleteMapping("{id}")
    public ResponseEntity<Void> delQuestion(@PathVariable("id") Long id,
                                            @AuthenticationPrincipal UserPrincipal user) {
        return ResponseEntity.noContent().build();
    }

}
