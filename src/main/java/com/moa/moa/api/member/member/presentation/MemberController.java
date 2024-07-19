package com.moa.moa.api.member.member.presentation;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/members")
@Tag(name = "v1-member", description = "회원 API")
public class MemberController {
    @Operation(summary = "내 회원 정보 조회")
    @SecurityRequirement(name = "bearerAuth")
    @GetMapping("me")
    public ResponseEntity<?> findMe() {
        return ResponseEntity.ok().body(null);
    }
}
