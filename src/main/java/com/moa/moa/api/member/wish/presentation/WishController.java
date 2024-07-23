package com.moa.moa.api.member.wish.presentation;

import com.moa.moa.api.member.wish.presentation.dto.AddWishExternalDto;
import com.moa.moa.api.member.wish.presentation.dto.FindAllWishExternalDto;
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
@Tag(name = "Wish-API", description = "렌탈샵 찜 API")
@SecurityRequirement(name = "bearerAuth")
@RequestMapping("/v1/wishes")
public class WishController {
    @Operation(summary = "렌탈샵 찜 추가")
    @PostMapping
    public ResponseEntity<AddWishExternalDto.Response> addWish(@Valid @RequestBody final AddWishExternalDto.Request request,
                                                               @AuthenticationPrincipal UserPrincipal user) {
        return ResponseEntity.created(null).body(null);
    }

    @Operation(summary = "렌탈샵 찜 목록 조회")
    @GetMapping
    public ResponseEntity<PageExternalDto.Response<List<FindAllWishExternalDto.Response>>> findAllWish(@RequestParam(name = "page", defaultValue = "0") int page,
                                                                                                      @RequestParam(name = "size", defaultValue = "10") int size,
                                                                                                      @AuthenticationPrincipal UserPrincipal user) {
        return ResponseEntity.ok().body(null);
    }

    @Operation(summary = "렌탈샵 찜 삭제")
    @DeleteMapping("{id}")
    public ResponseEntity<Void> delWish(@PathVariable("id") Long id,
                                        @AuthenticationPrincipal UserPrincipal user) {
        return ResponseEntity.noContent().build();
    }
}
