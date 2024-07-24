package com.moa.moa.api.shop.shop.presentation;

import com.moa.moa.api.shop.shop.presentation.dto.*;
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
@Tag(name = "Shop-API", description = "렌탈샵 API")
@RequestMapping("/v1/shops")
public class ShopController {
    @Operation(summary = "범위 내 렌탈샵 조회")
    @GetMapping
    public ResponseEntity<List<FindAllShopExternalDto.Response>> findAllShopWithinRange(@RequestParam(name = "leftTopX") Double leftTopX,
                                                                                        @RequestParam(name = "leftTopY") Double leftTopY,
                                                                                        @RequestParam(name = "rightBottomX") Double rightBottomX,
                                                                                        @RequestParam(name = "rightBottomY") Double rightBottomY) {

        return ResponseEntity.ok().body(null);
    }

    @Operation(summary = "렌탈샵 상세 조회")
    @GetMapping("{id}")
    public ResponseEntity<FindShopExternalDto.Response> findShop(@PathVariable("id") Long id) {

        return ResponseEntity.ok().body(null);
    }

    @Operation(summary = "최근 본 렌탈샵 조회")
    @SecurityRequirement(name = "bearerAuth")
    @GetMapping("recent")
    public ResponseEntity<?> findAllShopRecentlyViewed(@AuthenticationPrincipal UserPrincipal user) {

        return ResponseEntity.ok().body(null);
    }

    @Operation(summary = "최저가 렌탈샵 검색")
    @PutMapping("search")
    public ResponseEntity<PageExternalDto.Response<FindAllShopLowPriceExternalDto.Response>> findAllShopSearchForTheLowestPrice(@RequestParam(name = "page", defaultValue = "0") int page,
                                                                                                                                @RequestParam(name = "size", defaultValue = "10") int size,
                                                                                                                                @RequestParam(name = "hasNext") Boolean hasNext,
                                                                                                                                @Valid @RequestBody final FindAllShopLowPriceExternalDto.Request request) {

        return ResponseEntity.ok().body(null);
    }

    @Operation(summary = "렌탈샵 리뷰 조회")
    @GetMapping("{id}/reviews")
    public ResponseEntity<PageExternalDto.Response<FindAllShopReviewExternalDto.Response>> findAllReviewsForShop(@PathVariable("id") Long id,
                                                                                                                 @RequestParam(name = "page", defaultValue = "0") int page,
                                                                                                                 @RequestParam(name = "size", defaultValue = "10") int size,
                                                                                                                 @RequestParam(name = "hasNext") Boolean hasNext,
                                                                                                                 @RequestParam(name = "isPhoto") Boolean isPhoto,
                                                                                                                 @RequestParam(name = "filter", required = false) String filter) {

        return ResponseEntity.ok().body(null);
    }

    @Operation(summary = "렌탈샵 포토리뷰 썸네일 조회")
    @GetMapping("{id}/reviews/photo")
    public ResponseEntity<PageExternalDto.Response<List<FindAllShopPhotoReviewExternalDto.Response>>> findAllReviewsForShopPhoto(@PathVariable("id") Long id,
                                                                                                                                 @RequestParam(name = "page", defaultValue = "0") int page,
                                                                                                                                 @RequestParam(name = "size", defaultValue = "10") int size,
                                                                                                                                 @RequestParam(name = "hasNext") Boolean hasNext) {

        return ResponseEntity.ok().body(null);
    }
}
