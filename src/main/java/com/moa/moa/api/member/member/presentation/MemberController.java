package com.moa.moa.api.member.member.presentation;

import static com.moa.moa.global.common.response.ApiResponseCode.GET;

import com.moa.moa.api.member.member.presentation.dto.FindMemberExternalDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.nio.file.attribute.UserPrincipal;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/members")
@Tag(name = "Member-API", description = "회원 API")
public class MemberController {
    @Operation(summary = "내 회원 정보 조회", responses = {@ApiResponse(responseCode = GET)})
    @SecurityRequirement(name = "bearerAuth")
    @GetMapping("me")
    public ResponseEntity<FindMemberExternalDto.Response> findMe(@AuthenticationPrincipal UserPrincipal user) {
        return ResponseEntity.ok().body(null);
    }
}
