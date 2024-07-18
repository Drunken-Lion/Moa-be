package com.moa.moa.api.shop.review.presentation;

import com.moa.moa.api.shop.review.presentation.dto.AddReviewExternalDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@Tag(name = "v1-review", description = "리뷰 API")
@RequestMapping("/v1/reviews")
public class ReviewController {

    @Operation(summary = "리뷰 작성")
    @SecurityRequirement(name = "bearerAuth")
    @PostMapping
    public ResponseEntity<AddReviewExternalDto.Response> addReview(@Valid @RequestBody final AddReviewExternalDto.Request request) {

        return ResponseEntity.created(null).body(null);
    }
}
