package com.moa.moa.api.shop.shop.presentation;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@Tag(name = "Shop-API", description = "렌탈샵 API")
@RequestMapping("/v1/shops")
public class ShopController {
    @Operation(summary = "범위 내 렌탈샵 조회")
    @GetMapping
    public ResponseEntity<?> findAllShopWithinRange() {

        return ResponseEntity.ok().body(null);
    }

    @Operation(summary = "렌탈샵 상세 조회")
    @GetMapping("{id}")
    public ResponseEntity<?> findShop(@PathVariable("id") Long id) {

        return ResponseEntity.ok().body(null);
    }

    @Operation(summary = "최근 본 렌탈샵 조회")
    @SecurityRequirement(name = "bearerAuth")
    @GetMapping("recent")
    public ResponseEntity<?> findAllShopRecentlyViewed() {

        return ResponseEntity.ok().body(null);
    }

    @Operation(summary = "최저가 렌탈샵 검색")
    @PutMapping("search")
    public ResponseEntity<?> findAllShopSearchForTheLowestPrice() {

        return ResponseEntity.ok().body(null);
    }

    @Operation(summary = "렌탈샵 리뷰 조회")
    @GetMapping("{id}/reviews")
    public ResponseEntity<?> findAllReviewsForShop(@PathVariable("id") Long id) {

        return ResponseEntity.ok().body(null);
    }

    @Operation(summary = "렌탈샵 포토리뷰 썸네일 조회")
    @GetMapping("{id}/reviews/photo")
    public ResponseEntity<?> findAllReviewsForShopPhoto(@PathVariable("id") Long id) {

        return ResponseEntity.ok().body(null);
    }
}
