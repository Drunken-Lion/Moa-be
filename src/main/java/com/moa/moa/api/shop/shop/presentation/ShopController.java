package com.moa.moa.api.shop.shop.presentation;

import com.moa.moa.api.member.member.domain.entity.Member;
import com.moa.moa.api.member.member.domain.persistence.MemberRepository;
import com.moa.moa.api.shop.shop.application.ShopService;
import com.moa.moa.api.shop.shop.domain.dto.*;
import com.moa.moa.global.common.response.PageExternalDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.nio.file.attribute.UserPrincipal;
import java.util.List;
import java.util.Optional;

import static com.moa.moa.global.common.response.ApiResponseCode.GET;

@RestController
@RequiredArgsConstructor
@Tag(name = "Shop-API", description = "렌탈샵 API")
@RequestMapping("/v1/shops")
public class ShopController {
    private final ShopService shopService;
    private final MemberRepository memberRepository;

    @Operation(summary = "범위 내 렌탈샵 조회", responses = {@ApiResponse(responseCode = GET)})
    @GetMapping
    public ResponseEntity<List<FindAllShopDto.Response>> findAllShopWithinRange(@RequestParam(name = "leftTopX") Double leftTopX,
                                                                                @RequestParam(name = "leftTopY") Double leftTopY,
                                                                                @RequestParam(name = "rightBottomX") Double rightBottomX,
                                                                                @RequestParam(name = "rightBottomY") Double rightBottomY,
                                                                                @AuthenticationPrincipal UserPrincipal user) {
        // TODO : 회원 관련 기능이 완성되면 삭제할 것
        Member member = memberRepository.findByEmail("three@moa.com").get();

        List<FindAllShopDto.Response> responses = shopService.findAllShopWithinRange(leftTopX, leftTopY, rightBottomX, rightBottomY, member);
        return ResponseEntity.ok().body(responses);
    }

    @Operation(summary = "렌탈샵 상세 조회", responses = {@ApiResponse(responseCode = GET)})
    @GetMapping("{id}")
    public ResponseEntity<FindShopDto.Response> findShop(@PathVariable("id") Long id,
                                                         @AuthenticationPrincipal UserPrincipal user) {
        // TODO : 회원 관련 기능이 완성되면 삭제할 것
        Member member = memberRepository.findByEmail("three@moa.com").get();

        FindShopDto.Response response = shopService.findShop(id, member);
        return ResponseEntity.ok().body(response);
    }

    @Operation(summary = "최근 본 렌탈샵 조회", responses = {@ApiResponse(responseCode = GET)})
    @SecurityRequirement(name = "bearerAuth")
    @GetMapping("recent")
    public ResponseEntity<?> findAllShopRecentlyViewed(@AuthenticationPrincipal UserPrincipal user) {

        return ResponseEntity.ok().body(null);
    }

    @Operation(summary = "최저가 렌탈샵 검색", responses = {@ApiResponse(responseCode = GET)})
    @PutMapping("search")
    public ResponseEntity<PageExternalDto.Response<FindAllShopLowPriceDto.Response>> findAllShopSearchForTheLowestPrice(@RequestParam(name = "page", defaultValue = "0") int page,
                                                                                                                        @RequestParam(name = "size", defaultValue = "10") int size,
                                                                                                                        @Valid @RequestBody final FindAllShopLowPriceDto.Request request) {

        return ResponseEntity.ok().body(null);
    }

    @Operation(summary = "렌탈샵 리뷰 조회", responses = {@ApiResponse(responseCode = GET)})
    @GetMapping("{id}/reviews")
    public ResponseEntity<PageExternalDto.Response<FindAllShopReviewDto.Response>> findAllReviewsForShop(@PathVariable("id") Long id,
                                                                                                         @RequestParam(name = "page", defaultValue = "0") int page,
                                                                                                         @RequestParam(name = "size", defaultValue = "10") int size,
                                                                                                         @RequestParam(name = "isPhoto") Boolean isPhoto,
                                                                                                         @RequestParam(name = "filter", required = false) String filter) {

        return ResponseEntity.ok().body(null);
    }

    @Operation(summary = "최신리뷰의 대표이미지 리스트 조회", responses = {@ApiResponse(responseCode = GET)})
    @GetMapping("{id}/reviews/photo")
    public ResponseEntity<PageExternalDto.Response<List<FindAllShopPhotoReviewDto.Response>>> findAllReviewsForShopPhoto(@PathVariable("id") Long id,
                                                                                                                         @RequestParam(name = "page", defaultValue = "0") int page,
                                                                                                                         @RequestParam(name = "size", defaultValue = "10") int size) {

        return ResponseEntity.ok().body(null);
    }
}
