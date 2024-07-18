package com.moa.moa.api.member.custom.presentation;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@Tag(name = "v1-custom", description = "렌탈정보 API")
@SecurityRequirement(name = "bearerAuth")
@RequestMapping("/v1/customs")
public class CustomController {
    @Operation(summary = "내 스키어 추가")
    @PostMapping
    public ResponseEntity<?> addCustom() {

        return ResponseEntity.created(null).body(null);
    }

    @Operation(summary = "내 스키어 리스트 조회")
    @GetMapping
    public ResponseEntity<?> findAllCustom() {

        return ResponseEntity.ok().body(null);
    }

    @Operation(summary = "내 스키어 수정")
    @PutMapping("{id}")
    public ResponseEntity<?> modCustom(@PathVariable("id") Long id) {

        return ResponseEntity.ok().body(null);
    }

    @Operation(summary = "내 스키어 삭제")
    @DeleteMapping("{id}")
    public ResponseEntity<Void> delCustom(@PathVariable("id") Long id) {

        return ResponseEntity.noContent().build();
    }
}
