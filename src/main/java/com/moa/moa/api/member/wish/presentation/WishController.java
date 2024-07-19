package com.moa.moa.api.member.wish.presentation;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@Tag(name = "v1-wish", description = "렌탈샵 찜 API")
@SecurityRequirement(name = "bearerAuth")
@RequestMapping("/v1/wishes")
public class WishController {
    @Operation(summary = "렌탈샵 찜 추가")
    @PostMapping
    public ResponseEntity<?> addWish() {
        return ResponseEntity.created(null).body(null);
    }

    @Operation(summary = "렌탈샵 찜 목록 조회")
    @GetMapping
    public ResponseEntity<?> findAllWish() {
        return ResponseEntity.ok().body(null);
    }

    @Operation(summary = "렌탈샵 찜 삭제")
    @DeleteMapping("{id}")
    public ResponseEntity<Void> delWish(@PathVariable("id") Long id) {
        return ResponseEntity.noContent().build();
    }
}
