package com.moa.moa.api.shop.review.presentation;

import com.moa.moa.api.shop.review.presentation.dto.AddReviewExternalDto;
import com.moa.moa.api.shop.review.presentation.dto.FindAllReviewExternalDto;
import com.moa.moa.api.shop.review.presentation.dto.FindReviewExternalDto;
import com.moa.moa.api.shop.review.presentation.dto.ModReviewExternalDto;
import com.moa.moa.global.common.response.PageExternalDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import java.nio.file.attribute.UserPrincipal;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@Tag(name = "Review-API", description = "리뷰 API")
@RequestMapping("/v1/reviews")
public class ReviewController {

    @Operation(summary = "리뷰 작성")
    @SecurityRequirement(name = "bearerAuth")
    @PostMapping
    public ResponseEntity<AddReviewExternalDto.Response> addReview(@Valid @RequestBody final AddReviewExternalDto.Request request,
                                                                   @AuthenticationPrincipal UserPrincipal user) {

        return ResponseEntity.created(null).body(null);
    }

    @Operation(summary = "내가 쓴 리뷰 전체 조회")
    @SecurityRequirement(name = "bearerAuth")
    @GetMapping
    public ResponseEntity<PageExternalDto.Response<FindAllReviewExternalDto.Response>> findAllReview(@RequestParam(name = "page", defaultValue = "0") int page,
                                                                                            @RequestParam(name = "size", defaultValue = "10") int size,
                                                                                            @AuthenticationPrincipal UserPrincipal user) {
        return ResponseEntity.ok().body(null);
    }

    @Operation(summary = "리뷰 상세 조회")
    @GetMapping("/{id}")
    public ResponseEntity<FindReviewExternalDto.Response> findReview(@PathVariable Long id) {
        return ResponseEntity.ok().body(null);
    }

    @Operation(summary = "리뷰 수정")
    @SecurityRequirement(name = "bearerAuth")
    @PutMapping("/{id}")
    public ResponseEntity<ModReviewExternalDto.Response> modReview(@PathVariable Long id,
                                                                    @RequestBody ModReviewExternalDto.Request request,
                                                                    @AuthenticationPrincipal UserPrincipal user) {
        return ResponseEntity.ok().body(null);
    }

    @Operation(summary = "리뷰 삭제")
    @SecurityRequirement(name = "bearerAuth")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delReview(@PathVariable Long id,
                                          @AuthenticationPrincipal UserPrincipal user) {
        return ResponseEntity.noContent().build();
    }
}
